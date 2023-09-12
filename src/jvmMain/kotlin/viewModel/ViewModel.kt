package viewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlin.math.abs


enum class GameState {
    SUCCEED,
    FAIL,
    PROGRESS
}

data class Placement(val x: Int, val y: Int)


data class UIState(
    val placed: Set<Placement> = emptySet(),
    val numQueens: Int = 4,
    val safeGrid: Set<Placement> = List(numQueens * numQueens) {
        Placement(it / numQueens, it % numQueens)
    }.toSet(),
    val gameState: GameState = GameState.PROGRESS,
    val remain: Int = numQueens
)


class ViewModel {
    private val _uiStateFlow = MutableStateFlow(UIState())
    private val uiState
        get() = _uiStateFlow.value
    val uiStateFlow = _uiStateFlow.asStateFlow()
    private val UPPER = 10

    fun changeQueenSize(size: Int) {
        _uiStateFlow.update {
            UIState(
                placed = emptySet(),
                numQueens = size,
                safeGrid = List(size * size) {
                    Placement(it / size, it % size)
                }.toSet(),
                gameState = GameState.PROGRESS,
                remain = size
            )
        }
    }

    private fun fail() {
        _uiStateFlow.update {
            it.copy(gameState = GameState.FAIL)
        }
    }

    private fun succeed(placed: Set<Placement>, resume: () -> Unit) {
        _uiStateFlow.update {
            it.copy(gameState = GameState.SUCCEED)
        }
    }


    /**
     * [solveQueen]
     */
    fun solveQueen() {
        fun placeQueens(
            placed: Set<Placement>,
            left: Int,
            safe: Set<Placement>,
            f: () -> Unit,
            s: (Set<Placement>, () -> Unit) -> Unit
        ) {
            _uiStateFlow.update {
                it.copy(
                    placed = placed,
                    safeGrid = safe,
                    gameState = GameState.PROGRESS,
                    remain = left
                )
            }
            println(placed)
            println(left)
            runBlocking { delay(1000L) }

            if (left == 0) {
                s(placed, f)
            } else {
                if (safe.isEmpty()) {
                    f()
                } else {
                    placeQueens(
                        placed.plus(safe.first()),
                        left - 1,
                        pruneSquares(safe.first(), safe),
                        { (placeQueens(placed, left, safe.subtract(setOf(safe.first())), f, s)) },
                        s
                    )
                }
            }
        }

        placeQueens(placed = emptySet(), uiState.numQueens, uiState.safeGrid, ::fail, ::succeed)
    }


    private fun pruneSquares(q: Placement, safe: Set<Placement>): Set<Placement> {
        fun sameCol(s1: Placement, s2: Placement) = s1.x == s2.x
        fun sameRow(s1: Placement, s2: Placement) = s1.y == s2.y
        fun sameGrid(s1: Placement, s2: Placement) = (s1.y == s2.y) && (s1.x == s2.x)
        fun dia(s1: Placement, s2: Placement) = (abs(s1.x - s2.x) == abs(s1.y - s2.y))
        return safe.filter { s -> !(sameCol(s, q) || sameRow(s, q) || dia(s, q) || sameGrid(s, q)) }.toSet()
    }
}