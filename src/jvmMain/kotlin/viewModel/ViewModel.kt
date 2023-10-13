package viewModel

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs


enum class GameState {
    SUCCEED,
    FAIL,
    WAITING,
    SUCCESS_CONTINUATION,
    FAILURE_CONTINUATION,
    STACK_OVERFLOW
}

data class Placement(val x: Int, val y: Int)

data class UIState(
    val placed: Set<Placement> = emptySet(),
    val newUnsafe: Set<Placement> = emptySet(),
    val numQueens: Int = 4,
    val safeGrid: Set<Placement> = List(numQueens * numQueens) {
        Placement(it / numQueens, it % numQueens)
    }.toSet(),
    val gameState: GameState = GameState.WAITING,
    val remain: Int = numQueens,
)


class ViewModel {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private var resume: () -> Unit = ::fail
    private val _delayFLow = MutableStateFlow(1F)
    val delayFlow = _delayFLow.asStateFlow()
    private val maxDelay = 2000L
    private var delay
        get() = _delayFLow.value
        set(value) = _delayFLow.update { value }

    private var count = 4
    private val _uiStateFlow = MutableStateFlow(UIState(numQueens = count))
    private val uiState
        get() = _uiStateFlow.value
    val uiStateFlow = _uiStateFlow.asStateFlow()
    val upper = 7


    fun changeDelay(newDelay: Float) {
        delay = if (newDelay == 0.0F) {
            0.01F
        } else {
            newDelay
        }
    }

    fun plus() {
        count += 1
        changeQueenSize(count)
    }

    fun minus() {
        count -= 1
        changeQueenSize(count)
    }

    private fun changeQueenSize(size: Int) {
        _uiStateFlow.update {
            UIState(
                placed = emptySet(),
                numQueens = size,
                safeGrid = List(size * size) {
                    Placement(it / size, it % size)
                }.toSet(),
                newUnsafe = emptySet(),
                gameState = GameState.WAITING,
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
            it.copy(
                gameState = GameState.SUCCEED,
                newUnsafe = emptySet()
            )
        }
    }


    /**
     * [solveQueen]
     */
    suspend fun solveQueen() {
        fun placeQueens(
            placed: Set<Placement>,
            left: Int,
            safe: Set<Placement>,
            state: GameState,
            f: () -> Unit,
            s: (Set<Placement>, () -> Unit) -> Unit
        ) {
            var newPlaced: Placement? = null
            _uiStateFlow.update {
                newPlaced = placed.subtract(it.placed).firstOrNull()
                it.copy(
                    placed = placed,
                    gameState = state,
                )
            }
            if (uiState.gameState != GameState.WAITING) {
                runBlocking { delay((delay * maxDelay * 0.3).toLong()) }
            }
            _uiStateFlow.update {
                it.copy(
                    safeGrid = safe,
                    gameState = state,
                    remain = left,
                    newUnsafe = newPlaced?.let { g -> getUnsafe(g, it.numQueens) } ?: emptySet()
                )
            }
            if (uiState.gameState != GameState.WAITING) {
                runBlocking { delay((delay * maxDelay * 0.9).toLong()) }
            }
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
                        GameState.SUCCESS_CONTINUATION,
                        {
                            (placeQueens(
                                placed,
                                left,
                                safe.subtract(setOf(safe.first())),
                                GameState.FAILURE_CONTINUATION,
                                f,
                                s
                            ))
                        },
                        s
                    )
                }
            }
        }

        if (uiState.gameState == GameState.FAIL || uiState.gameState == GameState.SUCCEED) {
            changeQueenSize(uiState.numQueens)
        }

        scope.launch {
            try {
                placeQueens(
                    placed = emptySet(),
                    uiState.numQueens,
                    uiState.safeGrid,
                    uiState.gameState,
                    resume,
                    ::succeed
                )
            } catch (e: StackOverflowError) {
                _uiStateFlow.update { it.copy(gameState = GameState.STACK_OVERFLOW) }
            }
        }
    }
}


private fun pruneSquares(q: Placement, safe: Set<Placement>): Set<Placement> {
    fun sameCol(s1: Placement, s2: Placement) = s1.x == s2.x
    fun sameRow(s1: Placement, s2: Placement) = s1.y == s2.y
    fun dia(s1: Placement, s2: Placement) = (abs(s1.x - s2.x) == abs(s1.y - s2.y))
    val newUnsafe = safe.filter { s -> sameCol(s, q) || sameRow(s, q) || dia(s, q) }.toSet()
    return safe.subtract(newUnsafe.plus(q))
}

private fun getUnsafe(q: Placement, size: Int): Set<Placement> {
    fun sameCol(s1: Placement, s2: Placement) = s1.x == s2.x
    fun sameRow(s1: Placement, s2: Placement) = s1.y == s2.y
    fun dia(s1: Placement, s2: Placement) = (abs(s1.x - s2.x) == abs(s1.y - s2.y))
    return List(size * size) {
        Placement(it / size, it % size)
    }.filter { s -> sameCol(s, q) || sameRow(s, q) || dia(s, q) }.toSet().subtract(setOf(q))
}