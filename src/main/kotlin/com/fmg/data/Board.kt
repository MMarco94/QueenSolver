package com.fmg.data

abstract class Board(
    val size: Int,
    val queens: Set<Queen> = emptySet()
) {
    /**
     * @return a new board that contains the new given queen
     */
    abstract fun withQueen(queen: Queen): Board

    /**
     * @return a new board without the given queen
     */
    abstract fun withoutQueen(queen: Queen): Board

    abstract fun getNeighbors(): Sequence<Board>

    /**
     * @return Whether this board contains a valid queen configuration or not
     */
    open fun isValid() = queens.none { queen -> hasConflicts(queen) }

    fun isNQueenSolution() = queens.size == size && isValid()

    //TODO: use merge search?
    /**
     * @return whether a particular queen has conflicts
     */
    fun hasConflicts(queen: Queen) = queens.any { q2 -> q2.conflicts(queen) }


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
}