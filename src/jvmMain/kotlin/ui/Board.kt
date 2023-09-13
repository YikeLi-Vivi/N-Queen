package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewModel.Placement
import viewModel.UIState

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


@Preview
@Composable
private fun PreviewBoard() {
    Board()
}