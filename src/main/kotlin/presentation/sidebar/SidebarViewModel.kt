package presentation.sidebar

import core.viewmodel.ViewModel
import domain.model.Task
import domain.repository.ITaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SidebarViewModel(
    private val taskRepository: ITaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SidebarUiState())
    val uiState: StateFlow<SidebarUiState> = _uiState.asStateFlow()

    init {
        allTasks()
    }

    fun handleEvents(event: SidebarEvent) {
        viewModelScope.launch {
            when (event) {
                is SidebarEvent.CreateNewTask -> {
                    taskRepository.newTask(event.task)
                }
            }
        }
    }

    private fun allTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, tasks = null) }
            taskRepository.allTasks().collect { tasks ->
                _uiState.update { it.copy(loading = false, tasks = tasks) }
            }
        }
    }

}

data class SidebarUiState(
    val loading: Boolean = false,
    val tasks: List<Task>? = null,
    val tasksError: Throwable? = null,
)

sealed interface SidebarEvent {
    data class CreateNewTask(val task: Task) : SidebarEvent
}
