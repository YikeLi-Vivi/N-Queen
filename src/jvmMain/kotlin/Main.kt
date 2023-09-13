import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
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
import ui.Board
import ui.Marker
import ui.NumButton
import viewModel.GameState
import viewModel.Placement
import viewModel.UIState
import viewModel.ViewModel

@Composable
@Preview
fun App() {
    val viewModel = ViewModel()
    val state by viewModel.uiStateFlow.collectAsState()

    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            when (state.gameState) {
                GameState.WAITING -> Text("Click Solve", style = MaterialTheme.typography.h4)
                GameState.SUCCESS_CONTINUATION -> Text("success", style = MaterialTheme.typography.h4)
                GameState.FAILURE_CONTINUATION -> Text("resume", style = MaterialTheme.typography.h4)
                GameState.SUCCEED -> Text("SOLVED!", style = MaterialTheme.typography.h4)
                GameState.FAIL -> Text("FAILED", style = MaterialTheme.typography.h4)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Marker()
            Spacer(modifier = Modifier.height(20.dp))
            Board(
                state = state,
            )
            Spacer(modifier = Modifier.height(40.dp))
            NumButton(
                viewModel,
                minusEnabled = state.numQueens > 1 && state.gameState != GameState.SUCCESS_CONTINUATION && state.gameState != GameState.FAILURE_CONTINUATION,
                plusEnabled = state.numQueens < viewModel.upper && state.gameState != GameState.SUCCESS_CONTINUATION && state.gameState != GameState.FAILURE_CONTINUATION,
                count = state.numQueens
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.solveQueen()
                    }
                },
                enabled = state.gameState != GameState.SUCCESS_CONTINUATION && state.gameState != GameState.FAILURE_CONTINUATION,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text("Solve", style = MaterialTheme.typography.h5)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
