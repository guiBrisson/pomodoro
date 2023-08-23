package presentation.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.viewmodel.rememberViewModel
import ui.components.BaseContainer

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {
    val viewModel = rememberViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreen(
        modifier = modifier,
        uiState = uiState,
        onClose = onClose,
    )
}

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onClose: () -> Unit,
) {
    BaseContainer(
        modifier = modifier.fillMaxWidth(),
        internalPadding = PaddingValues(horizontal = 40.dp, vertical = 20.dp),
    ) {
        TextButton(
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
            onClick = onClose,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )
        }

    }

}
