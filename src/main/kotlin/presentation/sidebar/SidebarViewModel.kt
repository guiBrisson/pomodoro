package presentation.sidebar

import core.viewmodel.ViewModel
import domain.model.Task
import domain.repository.ITaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SidebarViewModel(
    private val taskRepository: ITaskRepository
) : ViewModel() {
    private val _allTasks = taskRepository.allTasks()
        .onStart { _uiState.update { it.copy(loadingTasks = true, tasks = null) } }
        .catch { t -> _uiState.update { it.copy(loadingTasks = false, tasksError = t) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _uiState = MutableStateFlow(SidebarUiState())
    val uiState: StateFlow<SidebarUiState> = combine(_allTasks, _uiState) { tasks, state ->
        state.copy(tasks = tasks, loadingTasks = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _uiState.value)

    fun handleEvents(event: SidebarEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is SidebarEvent.CreateNewTask -> {
                    taskRepository.newTask(event.task)
                }

                SidebarEvent.ClearAllTasks -> {
                    taskRepository.deleteAll()
                }

                SidebarEvent.ClearCompletedTasks -> {
                    taskRepository.deleteCompleted()
                }

                is SidebarEvent.DeleteTask -> {
                    event.task.id?.let { taskRepository.deleteTask(it) }
                }

                is SidebarEvent.EditTask -> {
                    taskRepository.updateTask(event.task)
                }

                is SidebarEvent.MarkTaskAsDone -> {
                    val totalAmount = event.task.totalAmount
                    taskRepository.updateTask(event.task.copy(isCompleted = true, amountDone = totalAmount))
                }

                is SidebarEvent.RestartTask -> {
                    taskRepository.updateTask(event.task.copy(isCompleted = false, amountDone = 0))
                }
            }
        }
    }

}

data class SidebarUiState(
    val loadingTasks: Boolean = true,
    val tasks: List<Task>? = null,
    val tasksError: Throwable? = null,
)

sealed interface SidebarEvent {
    data class CreateNewTask(val task: Task) : SidebarEvent
    object ClearCompletedTasks : SidebarEvent
    object ClearAllTasks : SidebarEvent
    data class EditTask(val task: Task) : SidebarEvent
    data class MarkTaskAsDone(val task: Task) : SidebarEvent
    data class RestartTask(val task: Task) : SidebarEvent
    data class DeleteTask(val task: Task) : SidebarEvent
}
