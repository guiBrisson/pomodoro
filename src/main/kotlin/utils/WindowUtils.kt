package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope

object WindowUtils {
    fun isMacOSX(): Boolean {
        val osName = System.getProperty("os.name")
        return "OS X" == osName || "Mac OS X" == osName
    }
}

@Composable
fun FrameWindowScope.setupMac() {
    if (WindowUtils.isMacOSX()) {
        window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
        window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
        window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
    }
}