package ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.Task
import ui.utils.loadSvgPainter

@Composable
fun TasksComponent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    tasks: List<Task>?,
    onEdit: (Task) -> Unit,
    onDone: (Task) -> Unit,
    onRestart: (Task) -> Unit,
    onDelete: (Task) -> Unit,
) {
    var selectedTask: Task? by remember { mutableStateOf(null) }

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            tasks?.let { tasks ->
                if (tasks.isEmpty()) {
                    TaskEmptyState()
                } else {
                    Column {
                        TaskTitle()
                        Divider(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp))
                        LazyColumn(modifier = modifier.fillMaxSize()) {
                            items(tasks) { task ->
                                TaskItem(
                                    task = task,
                                    isSelected = task == selectedTask,
                                    onClick = { selectedTask = task },
                                    onEdit = { onEdit(task) },
                                    onDone = { onDone(task) },
                                    onRestart = { onRestart(task) },
                                    onDelete = {
                                        if (task == selectedTask) selectedTask = null
                                        onDelete(task)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}


@Composable
private fun TaskEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(painter = loadSvgPainter("images/img_empty_state.svg"), contentDescription = null)
        Text(
            text = "No tasks here!",
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = "Click on tha Add Task button below to create a new task.",
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun TaskTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 24.dp, start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("My Tasks", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

        DotsMenuIconButton(onClick = { })
    }
}

@Composable
private fun DotsMenuIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val mutableInteractions = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(mutableInteractions, indication = null, onClick = onClick)
            .padding(2.dp)
            .pointerHoverIcon(PointerIcon.Hand),
    ) {
        Icon(painter = loadSvgPainter("icons/ic_vertical_menu.svg"), contentDescription = null)
    }
}

@Composable
private fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDone: () -> Unit,
    onRestart: () -> Unit,
    onDelete: () -> Unit,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colors.onSurface.copy(alpha = 0.15f) else Color.Unspecified
    var dropdownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val nameColor = if (task.isCompleted) {
            MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        } else {
            MaterialTheme.colors.onSurface
        }

        Text(
            modifier = Modifier.weight(1f),
            text = task.name,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
            color = nameColor,
        )

        Box {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                val text = if (task.isCompleted) "All done!" else "${task.amountDone}/${task.totalAmount}"
                Text(text = text, fontSize = 12.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f))
                DotsMenuIconButton(onClick = { dropdownExpanded = true })
            }
            TaskDropdownMenu(
                modifier = Modifier.align(Alignment.TopEnd),
                expanded = dropdownExpanded,
                task = task,
                onDismissRequest = { dropdownExpanded = false },
                onEdit = onEdit,
                onDone = onDone,
                onRestart = onRestart,
                onDelete = onDelete,
            )
        }
    }
}

@Composable
fun TaskDropdownMenu(
    modifier: Modifier = Modifier,
    task: Task,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEdit: () -> Unit,
    onDone: () -> Unit,
    onRestart: () -> Unit,
    onDelete: () -> Unit,
) {
    val itemTextStyle = TextStyle(fontSize = 14.sp)

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        val itemModifier = Modifier.height(32.dp).pointerHoverIcon(PointerIcon.Hand)
        DropdownMenuItem(modifier = itemModifier, onClick = { onDismissRequest(); onEdit() }) {
            Text("Edit", style = itemTextStyle)
        }

        if (task.isCompleted) {
            DropdownMenuItem(modifier = itemModifier, onClick = { onDismissRequest(); onRestart() }) {
                Text("Restart", style = itemTextStyle)
            }
        } else {
            DropdownMenuItem(modifier = itemModifier, onClick = { onDismissRequest(); onDone() }) {
                Text("Mark as done", style = itemTextStyle)
            }
        }

        DropdownMenuItem(modifier = itemModifier, onClick = { onDismissRequest(); onDelete() }) {
            Text("Delete", style = itemTextStyle)
        }
    }
}
