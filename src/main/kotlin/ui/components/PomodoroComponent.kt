package ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.Task
import ui.utils.loadSvgPainter
import utils.PomodoroEvent
import utils.PomodoroSettings

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PomodoroComponent(
    modifier: Modifier = Modifier,
    isCollapsed: Boolean,
    selectedTask: Task?,
    timerValue: Int,
    isTimeRunning: Boolean,
    currentPomodoroEvent: PomodoroEvent,
    onSetting: () -> Unit,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
) {
    val timerTotal = when (currentPomodoroEvent) {
        PomodoroEvent.FOCUS -> PomodoroSettings.focusTimeInSeconds
        PomodoroEvent.REST -> PomodoroSettings.restTimeInSeconds
        PomodoroEvent.LONG_REST -> PomodoroSettings.longRestTimeInSeconds
    }

    val progress: Float by animateFloatAsState(
        targetValue = timerValue.toFloat() / timerTotal
    )

    val isTaskComplete = (selectedTask?.isCompleted == true)
    val titleText = if (isTaskComplete) {
        "TASK COMPLETED"
    } else {
        currentPomodoroEvent.title
    }

    AnimatedContent(isCollapsed) { collapsed ->
        if (collapsed) {
            CollapsedPomodoro(
                modifier = modifier,
                title = titleText,
                progress = progress,
                timer = formatSecondsToMinutesSeconds(timerValue),
                isTimeRunning = isTimeRunning,
                selectedTask = selectedTask,
                onResume = onResume,
                onPause = onPause,
                onNext = onNext,
            )
        } else {
            ExpandedPomodoro(
                modifier = modifier.fillMaxSize(),
                title = titleText,
                progress = progress,
                timer = formatSecondsToMinutesSeconds(timerValue),
                isTimeRunning = isTimeRunning,
                selectedTask = selectedTask,
                onSetting = onSetting,
                onResume = onResume,
                onPause = onPause,
                onNext = onNext,
            )
        }
    }
}

@Composable
private fun CollapsedPomodoro(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
    timer: String,
    selectedTask: Task?,
    isTimeRunning: Boolean,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
) {
    BaseContainer(
        modifier = modifier.fillMaxWidth().height(100.dp),
        internalPadding = PaddingValues(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 40.dp).weight(1f).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TimerController(
                isTimeRunning = isTimeRunning,
                itemsSpacedBy = 16.dp,
                iconSize = 28.dp,
                onPause = onPause,
                onResume = onResume,
                onNext = onNext,
            )

            selectedTask?.let { selectedTask ->
                Text(
                    modifier = Modifier.weight(1f).padding(horizontal = 40.dp),
                    text = selectedTask.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = timer,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }
        }

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().height(8.dp),
            progress = progress,
            strokeCap = StrokeCap.Round,
        )
    }

}

@Composable
private fun ExpandedPomodoro(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
    timer: String,
    selectedTask: Task?,
    isTimeRunning: Boolean,
    onSetting: () -> Unit,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
) {
    BaseContainer {
        Box(modifier = modifier) {

            SettingButtons(modifier = Modifier.align(Alignment.TopEnd), onClick = onSetting)

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val isTaskComplete = (selectedTask?.isCompleted == true)


                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                )
                if (!isTaskComplete) {
                    selectedTask?.let { selectedTask ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = selectedTask.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.onSurface,
                            )
                            Text(
                                text = "#${selectedTask.amountDone()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(bottom = 40.dp))

                Box(modifier = Modifier.size(400.dp)) {
                    ComposeCircularProgressBar(
                        modifier = Modifier.fillMaxSize(),
                        percentage = progress,
                        fillColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                        strokeWidth = 16.dp,
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = timer,
                            fontSize = 80.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface
                        )

                        Spacer(modifier = Modifier.padding(vertical = 20.dp))

                        TimerController(
                            isTimeRunning = isTimeRunning,
                            onPause = onPause,
                            onResume = onResume,
                            onNext = onNext,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerController(
    isTimeRunning: Boolean,
    itemsSpacedBy: Dp = 36.dp,
    iconSize: Dp = 32.dp,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(itemsSpacedBy),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //Todo: change this button to 'restart'
        IconButton(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand), onClick = { }) {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = loadSvgPainter("icons/ic_stop.svg"),
                contentDescription = null
            )
        }

        if (isTimeRunning) {
            IconButton(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand), onClick = onPause) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = loadSvgPainter("icons/ic_pause.svg"),
                    contentDescription = null
                )
            }
        } else {
            IconButton(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand), onClick = onResume) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = loadSvgPainter("icons/ic_play.svg"),
                    contentDescription = null
                )
            }
        }

        IconButton(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand), onClick = onNext) {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = loadSvgPainter("icons/ic_next.svg"),
                contentDescription = null
            )
        }

    }
}

@Composable
private fun SettingButtons(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick() }
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.05f))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Default.Settings,
            contentDescription = null,
        )

        Text(text = "Settings", fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ComposeCircularProgressBar(
    modifier: Modifier = Modifier,
    percentage: Float,
    fillColor: Color,
    backgroundColor: Color,
    strokeWidth: Dp
) {
    Canvas(
        modifier = modifier
            .size(150.dp)
            .padding(10.dp)
    ) {

        drawArc(
            color = backgroundColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
            size = Size(size.width, size.height)
        )

        drawArc(
            color = fillColor,
            startAngle = -90f,
            sweepAngle = percentage * -360f,
            useCenter = false,
            style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
            size = Size(size.width, size.height)
        )
    }
}

fun formatSecondsToMinutesSeconds(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "$minutes:${String.format("%02d", remainingSeconds)}"
}
