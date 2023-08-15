import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.formdev.flatlaf.themes.FlatMacDarkLaf
import data.datasource.local.di.localDataModule
import di.viewModelsModule
import domain.model.Task
import org.koin.core.context.startKoin
import presentation.sidebar.SidebarScreen
import ui.components.BaseContainer
import ui.theme.PomodoroTheme
import utils.OSUtils
import utils.setupMac
import utils.setupWindows
import javax.swing.UIManager

fun main() = application {
    val state = rememberWindowState(width = 1280.dp, height = 832.dp)

    UIManager.setLookAndFeel(FlatMacDarkLaf())

    startKoin {
        modules(localDataModule, viewModelsModule)
    }

    Window(onCloseRequest = ::exitApplication, state = state) {
        setupMac()
        setupWindows()

        App()
    }
}

@Composable
fun App() {
    PomodoroTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            val macPadding = if (OSUtils.isMacOSX()) PaddingValues(top = 18.dp) else PaddingValues()
            Row(
                modifier = Modifier.padding(macPadding).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var selectedTask: Task? by remember { mutableStateOf(null) }

                SidebarScreen(onTaskSelected = { selectedTask = it })
                BaseContainer(modifier = Modifier.fillMaxSize()) {
                    selectedTask?.let {
                        Text(it.name)
                    }
                }
            }
        }
    }
}
