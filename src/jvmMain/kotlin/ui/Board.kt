package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewModel.GameState
import viewModel.Placement
import viewModel.UIState
import viewModel.ViewModel


@Composable
fun Board(
    modifier: Modifier = Modifier.size(500.dp),
    state: UIState = UIState(),
) {
    val gridSize = 500 / state.numQueens
    LazyVerticalGrid(
        columns = GridCells.Fixed(state.numQueens), modifier = modifier,
        verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.Center
    ) {
        items(count = state.numQueens * state.numQueens) { index ->
            val placement = Placement(x = index / state.numQueens, y = index % state.numQueens)
            if (placement in state.placed) {
                QueenGrid(gridSize.dp)
            } else if (placement in state.safeGrid) {
                SafeGrid(gridSize.dp)
            } else if (placement in state.newUnsafe) {
                NewUnsafeGrid(gridSize.dp)
            } else {
                UnsafeGrid(gridSize.dp)
            }
        }
    }
}


@Composable
fun GameStateDisplay(
    state: GameState
) {
    val backgroundColor = when (state) {
        GameState.WAITING -> ColorTheme.Waiting
        GameState.FAIL -> ColorTheme.Failed
        GameState.SUCCEED -> ColorTheme.Succeed
        GameState.SUCCESS_CONTINUATION -> ColorTheme.SuccessContinuation
        GameState.FAILURE_CONTINUATION -> ColorTheme.ResumeContinuation
    }

    Box(
        modifier = Modifier.background(backgroundColor).width(250.dp).height(100.dp).padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (state) {
                GameState.WAITING -> "Click Solve"
                GameState.FAIL -> "Failed :("
                GameState.SUCCEED -> "Solved!"
                GameState.SUCCESS_CONTINUATION -> "Succeed to Next Step"
                GameState.FAILURE_CONTINUATION -> "Backtrack to Previous State"
            }, color = Color.White, style = MaterialTheme.typography.h6, textAlign = TextAlign.Center
        )
    }

}


@Preview
@Composable
private fun PreviewGameState() {
    GameStateDisplay(GameState.FAILURE_CONTINUATION)
}

@Preview
@Composable
private fun PreviewBoard() {
    Board()
}

@Composable
fun AppView(
) {
    val viewModel = ViewModel()
    val state by viewModel.uiStateFlow.collectAsState()
    val delay by viewModel.delayFlow.collectAsState()

    MaterialTheme {
        Row(horizontalArrangement = Arrangement.Center) {
            Column(
                modifier = Modifier.weight(0.5f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameStateDisplay(state.gameState)
                Spacer(modifier = Modifier.height(100.dp))
                SpeedSlider(value = delay, onValueChange = viewModel::changeDelay)
                Spacer(modifier = Modifier.height(15.dp))
                NumButton(
                    viewModel,
                    minusEnabled = state.numQueens > 1 &&
                            state.gameState != GameState.SUCCESS_CONTINUATION &&
                            state.gameState != GameState.FAILURE_CONTINUATION,
                    plusEnabled = state.numQueens < viewModel.upper &&
                            state.gameState != GameState.SUCCESS_CONTINUATION &&
                            state.gameState != GameState.FAILURE_CONTINUATION,
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
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        disabledBackgroundColor = Color.LightGray
                    )
                ) {
                    Text("Solve", style = MaterialTheme.typography.h5, color = Color.White)
                }
                Spacer(modifier = Modifier.height(15.dp))
            }

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Board(
                    state = state
                )
                Spacer(modifier = Modifier.height(30.dp))
                Marker()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAppView() {
    AppView()
}