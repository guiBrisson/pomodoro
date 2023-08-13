package ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun SearchComponent(
    modifier: Modifier = Modifier,
    onSearch: (input: String) -> Unit,
) {
    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(currentState)

    val width by transition.animateDp { state ->
        when (state) {
            BoxState.Collapsed -> 44.dp
            BoxState.Expanded -> 400.dp
        }
    }

    var input by remember { mutableStateOf("") }

    LaunchedEffect(input) {
        onSearch(input)
    }

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.05f)),
    ) {
        val textStyle = MaterialTheme.typography.subtitle1
        BasicTextField(
            modifier = Modifier.animateContentSize(),
            value = input,
            onValueChange = { input = it },
            cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
            textStyle = textStyle.copy(color = MaterialTheme.colors.onSurface),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(modifier = Modifier.width(width), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        currentState = if (currentState == BoxState.Expanded) {
                            BoxState.Collapsed
                        } else {
                            BoxState.Expanded
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colors.onSurface)
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (input.isEmpty() && currentState == BoxState.Expanded) {
                            Text(
                                text = "Search...",
                                style = textStyle.copy(color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
                            )
                        }
                        innerTextField()
                    }

                    if (input.isNotEmpty()) {
                        IconButton(
                            modifier = Modifier,
                            onClick = { input = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null, tint = MaterialTheme.colors.onSurface)
                        }
                    }
                }
            }
        )

    }
}

private enum class BoxState {
    Collapsed,
    Expanded
}

