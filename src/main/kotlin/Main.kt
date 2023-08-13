import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import presentation.sidebar.SidebarScreen
import ui.components.BaseContainer
import ui.theme.PomodoroTheme
import utils.setupMac

fun main() = application {
    val state = rememberWindowState(width = 1280.dp, height = 832.dp)

    Window(onCloseRequest = ::exitApplication, state = state) {
        setupMac()
        App()
    }
}

@Composable
fun App() {
    PomodoroTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                var arg by mutableStateOf("")

                SidebarScreen(onSendArgs = { arg = it })
                BaseContainer(modifier = Modifier.fillMaxSize()) {
                    Text(arg)
                }
            }
        }
    }
}
