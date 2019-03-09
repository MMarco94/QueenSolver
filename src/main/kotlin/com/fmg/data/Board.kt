package com.fmg.data

/**
 * A class that represents a chess board
 */
class Board private constructor(
    val size: Int,
    val queensDisposition: QueensDisposition
) {
    val queens: Set<Queen> = queensDisposition.queens

    constructor(size: Int) : this(size, QueensDisposition(size))

    /**
     * @return a new board that contains the new given queen
     */
    fun withQueen(queen: Queen) = Board(size, queensDisposition.withQueen(queen))

    /**
     * @return a new board without the given queen
     */
    fun withoutQueen(queen: Queen) = Board(size, queensDisposition.withoutQueen(queen))

    fun isNQueenSolution() = queens.size == size && !queensDisposition.hasConflicts()

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