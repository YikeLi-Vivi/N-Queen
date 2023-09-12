package viewModel

import androidx.compose.foundation.gestures.rememberTransformableState
import kotlin.math.abs


fun fail() = println("no-solution")
fun success(placed: List<Placement>, resume: () -> Unit) {
    println("nQueens solved")
}


fun emptyBoard(n: Int): List<Placement> {
    return List(n * n) { index ->
        Placement(index / n, index % n)
    }
}
