package utils

import androidx.compose.ui.window.FrameWindowScope
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.awt.Color


object OSUtils {
    fun isMacOSX(): Boolean {
        return (hostOs == OS.MacOS)

    }

    fun isWindowsOS(): Boolean {
        return (hostOs == OS.Windows)
    }
}

fun FrameWindowScope.setupMac() {
    if (OSUtils.isMacOSX()) {
        window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
        window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
        window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
    }
}

fun FrameWindowScope.setupWindows() {
    if (OSUtils.isWindowsOS()) {
        window.rootPane.putClientProperty("JRootPane.titleBarBackground", Color.black)
        window.rootPane.putClientProperty("JRootPane.titleBarForeground", Color.white)
    }
}
