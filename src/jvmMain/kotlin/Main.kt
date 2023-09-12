import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.Board
import viewModel.Placement
import viewModel.UIState
import viewModel.ViewModel

@Composable
@Preview
fun App() {
    val viewModel = ViewModel()
    val state by viewModel.uiStateFlow.collectAsState()

    CoroutineScope(Dispatchers.Default).launch {
        viewModel.solveQueen()
    }

    MaterialTheme {
        Board(
            state = state,
        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
