package ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun PomodoroTheme(
    content: @Composable () -> Unit,
) {

    MaterialTheme(
        typography = Typography,
        colors = darkColors,
        content = content,
    )
}
