package presentation.sidebar

import core.viewmodel.ViewModel
import domain.model.Task
import domain.repository.ITaskRepository
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
        viewModelScope.launch {
            when (event) {
                is SidebarEvent.CreateNewTask -> {
                    taskRepository.newTask(event.task)
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
}
