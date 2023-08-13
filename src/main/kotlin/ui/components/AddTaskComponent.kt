package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddTaskComponent(
    modifier: Modifier = Modifier,
) {
    var currentState by remember { mutableStateOf(AddTaskComponentState.Collapsed) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        AddTaskButton(state = currentState, onClick = {
            currentState = if (currentState == AddTaskComponentState.Collapsed) {
                AddTaskComponentState.Expanded
            } else {
                AddTaskComponentState.Collapsed
            }
        })
        AnimatedVisibility(currentState == AddTaskComponentState.Expanded) {
            AddTaskBox(
                onCancel = {
                   currentState = AddTaskComponentState.Collapsed
                },
                onSave = {
                    currentState = AddTaskComponentState.Collapsed
                },
            )
        }
    }

}

@Composable
private fun AddTaskButton(
    modifier: Modifier = Modifier,
    state: AddTaskComponentState,
    onClick: () -> Unit,
) {
    val shape = if (state == AddTaskComponentState.Expanded) {
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    } else {
        RoundedCornerShape(12.dp)
    }

    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.05f))
            .clickable { onClick() }
            .pointerHoverIcon(icon = PointerIcon.Hand),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Text(modifier = Modifier.padding(start = 8.dp), text = "Add Task", style = MaterialTheme.typography.button)
    }
}

@Composable
private fun AddTaskBox(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    val shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
    val focusRequester = remember { FocusRequester() }

    var name by remember { mutableStateOf("") }
    var times: Int? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.05f))
            .padding(20.dp),
    ) {
        val textStyle = MaterialTheme.typography.subtitle1


        BasicTextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = name,
            onValueChange = { name = it },
            textStyle = textStyle.copy(color = MaterialTheme.colors.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (name.isEmpty()) {
                        Text(
                            text = "What are you working on?",
                            style = textStyle.copy(color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)),
                        )
                    }
                    innerTextField()
                }
            }
        )

        Text(modifier = Modifier.padding(top = 18.dp), text = "Est Pomodoros", fontWeight = FontWeight.Medium)

        Row(modifier = Modifier.padding(top = 4.dp)) {
            BasicTextField(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(width = 75.dp, height = 35.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)),
                value = "${times ?: ""}",
                onValueChange = {
                    if (it.matches(Regex("-?\\d*(\\.\\d+)?")) && it.length <= 3) {
                        times = if (it.isEmpty()) null else it.toInt()
                    }
                },
                textStyle = textStyle.copy(color = MaterialTheme.colors.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                        innerTextField()
                    }
                }
            )

            BaseAddTaskButton(
                onClick = {
                    if (times == null) {
                        times = 1
                        return@BaseAddTaskButton
                    }
                    times = times?.plus(1)
                }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
            }

            BaseAddTaskButton(
                modifier = Modifier.padding(start = 4.dp),
                onClick = {
                    if (times != null && times!! <= 0) return@BaseAddTaskButton
                    times = times?.minus(1)
                }
            ) {
                Icon(
                    modifier = Modifier.rotate(180f),
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 22.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)),
            ) {
                Text(text = "Cancel", style = MaterialTheme.typography.button)
            }

            Button(
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).padding(start = 12.dp),
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff222222),
                    contentColor = MaterialTheme.colors.onSurface,
                )
            ) {
                Text(text = "Save", style = MaterialTheme.typography.button)
            }
        }
    }
}

@Composable
private fun BaseAddTaskButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier
            .size(35.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f))
            .pointerHoverIcon(PointerIcon.Hand),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

private enum class AddTaskComponentState {
    Collapsed,
    Expanded
}