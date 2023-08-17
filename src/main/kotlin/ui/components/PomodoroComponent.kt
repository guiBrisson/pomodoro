package ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.utils.loadSvgPainter
import utils.PomodoroEvent
import utils.PomodoroSettings

@Composable
fun PomodoroComponent(
    modifier: Modifier = Modifier,
    timerValue: Int,
    isTimeRunning: Boolean,
    currentPomodoroEvent: PomodoroEvent,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
) {
    val timerTotal = when(currentPomodoroEvent) {
        PomodoroEvent.FOCUS -> PomodoroSettings.focusTimeInSeconds
        PomodoroEvent.REST -> PomodoroSettings.restTimeInSeconds
        PomodoroEvent.LONG_REST -> PomodoroSettings.longRestTimeInSeconds
    }

    val progress: Float by animateFloatAsState(
        targetValue = timerValue.toFloat() / timerTotal
    )

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.padding(bottom = 40.dp),
            text = currentPomodoroEvent.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        )
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
                    text = formatSecondsToMinutesSeconds(timerValue),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.padding(vertical = 20.dp))

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(36.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //Todo: remove this button
                    IconButton(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand), onClick = { }) {
                        Icon(painter = loadSvgPainter("icons/ic_stop.svg"), contentDescription = null)
                    }

                    if (isTimeRunning) {
                        IconButton(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand), onClick = onPause) {
                            Icon(painter = loadSvgPainter("icons/ic_pause.svg"), contentDescription = null)
                        }
                    } else {
                        IconButton(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand), onClick = onResume) {
                            Icon(painter = loadSvgPainter("icons/ic_play.svg"), contentDescription = null)
                        }
                    }

                    IconButton(modifier = Modifier.pointerHoverIcon(PointerIcon.Hand), onClick = onNext) {
                        Icon(painter = loadSvgPainter("icons/ic_next.svg"), contentDescription = null)
                    }

                }
            }
        }
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
