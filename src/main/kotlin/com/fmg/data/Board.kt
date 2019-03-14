package com.fmg.data

/**
 * A class that represents a chess board
 */
class Board private constructor(
    val size: Int,
    val queensDisposition: QueensDisposition
) {
    val queens: Set<Queen>
        get() = queensDisposition.queens

    constructor(size: Int) : this(size, QueensDisposition(size))

    /**
     * @return a new board that contains the new given queen
     */
    fun withQueen(queen: Queen) = Board(size, queensDisposition.withQueen(queen))

    /**
     * @return a new board without the given queen
     */
    fun withoutQueen(queen: Queen) = Board(size, queensDisposition.withoutQueen(queen))

    fun with(
        toAddQueens: Array<Queen> = emptyArray(),
        toRemoveQueens: Array<Queen> = emptyArray()
    ) = Board(size, queensDisposition.with(toAddQueens, toRemoveQueens))

    fun isNQueenSolution() = queens.size == size && !queensDisposition.hasConflicts()

    operator fun times(another: Board): Board {
        return Board(size * another.size).with(toAddQueens = another.queens.flatMap { q1 ->
            queens.map { q2 ->
                Queen(q1.row * size + q2.row, q1.col * size + q2.col)
            }
        }.toTypedArray())
    }

    /**
     * This method prints the board
     */
    fun print() {
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (queens.contains(Queen(r, c))) {
                    print("x")
                } else {
                    print(" ")
                }
                print("|")
            }
            println()
        }
    }

    fun withScore(score: Double) = BoardWithScore(this, score)
    fun withScore(evaluator: BoardEvaluator) = withScore(evaluator.evaluate(this))

    companion object {

        fun generateAllQueens(size: Int): Sequence<Queen> {
            return (0 until size).asSequence()
                .flatMap { row ->
                    (0 until size).asSequence()
                        .map { col -> Queen(row, col) }
                }
        }
    }
}

data class BoardWithScore(val board: Board, val score: Double) {
    fun withAnnealing(annealingSteps: Int = 0) =
        BoardWithScoreAndAnnealing(BoardWithScore(board, score), annealingSteps);
}

data class BoardWithScoreAndAnnealing(val boardWithScore: BoardWithScore, val stepsFromRennealing: Int = 0)