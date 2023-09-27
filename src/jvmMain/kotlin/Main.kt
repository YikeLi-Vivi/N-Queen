import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.AppView
import ui.Board
import ui.Marker
import ui.NumButton
import viewModel.GameState
import viewModel.Placement
import viewModel.UIState
import viewModel.ViewModel


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        AppView()
    }
}
