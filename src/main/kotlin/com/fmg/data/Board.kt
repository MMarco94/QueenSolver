package com.fmg.data

class Board(
    val size: Int,
    val queens: Set<Queen> = emptySet()
) {

    /**
     * @return a new board that contains the new given queen
     */
    fun withQueen(queen: Queen) = Board(size, queens + queen)

    /**
     * @return a new board without the given queen
     */
    fun withoutQueen(queen: Queen) = Board(size, queens - queen)

    /**
     * @return Whether this board contains a valid queen configuration or not
     */
    fun isValid() = queens.none { queen -> hasConflicts(queen) }

    fun isNQueenSolution() = queens.size == size && isValid()

    //TODO: use merge search?
    /**
     * @return whether a particular queen has conflicts
     */
    fun hasConflicts(queen: Queen) = queens.any { q2 -> queen != q2 && q2.conflicts(queen) }

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