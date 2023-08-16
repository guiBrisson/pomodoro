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
    val uiState: StateFlow<SidebarUiState> = stateFlow()

    private fun stateFlow() = combine(_allTasks, _uiState) { tasks, state ->
        state.selectedTask?.let { selectedTask ->
            tasks?.find { it.id == selectedTask.id }?.let {
                updateSelectedTask(it)
            }
        }
        state.copy(tasks = tasks, loadingTasks = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _uiState.value)

    fun handleEvents(event: SidebarEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is SidebarEvent.SelectTask -> updateSelectedTask(event.task)

                is SidebarEvent.CreateNewTask -> {
                    taskRepository.newTask(event.task)
                }

                SidebarEvent.ClearAllTasks -> {
                    taskRepository.deleteAll()
                    unselectedCurrentTask()
                }

                SidebarEvent.ClearCompletedTasks -> {
                    taskRepository.deleteCompleted()
                    unselectedCurrentTask()
                }

                is SidebarEvent.DeleteTask -> {
                    event.task.id?.let {
                        taskRepository.deleteTask(it)

                        if (_uiState.value.selectedTask?.id == it) {
                            unselectedCurrentTask()
                        }
                    }
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

    private fun unselectedCurrentTask() {
        _uiState.update { it.copy(selectedTask = null) }
    }

    private fun updateSelectedTask(task: Task?) {
        _uiState.update { it.copy(selectedTask = task) }
    }

}

data class SidebarUiState(
    val loadingTasks: Boolean = true,
    val tasks: List<Task>? = null,
    val tasksError: Throwable? = null,
    val selectedTask: Task? = null,
)

sealed interface SidebarEvent {
    data class SelectTask(val task: Task?) : SidebarEvent
    data class CreateNewTask(val task: Task) : SidebarEvent
    object ClearCompletedTasks : SidebarEvent
    object ClearAllTasks : SidebarEvent
    data class EditTask(val task: Task) : SidebarEvent
    data class MarkTaskAsDone(val task: Task) : SidebarEvent
    data class RestartTask(val task: Task) : SidebarEvent
    data class DeleteTask(val task: Task) : SidebarEvent
}
