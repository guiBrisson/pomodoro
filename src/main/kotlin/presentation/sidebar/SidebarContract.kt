package presentation.sidebar

import domain.model.Task

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