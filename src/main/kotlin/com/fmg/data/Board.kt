package com.fmg.data

/**
 * This class represents a chess board size x size.
 *
 * Any board representable by this class MUST be obtainable by subsequent calls to getNeighbors
 */
abstract class Board(
    val size: Int
) {

    /**
     * @return All the queens in the board
     */
    abstract fun getQueens(): Collection<Queen>

    /**
     * @return a new board that contains the new given queen
     */
    abstract fun withQueen(queen: Queen): Board

    /**
     * @return a new board without the given queen
     */
    abstract fun withoutQueen(queen: Queen): Board

    /**
     * @return Whether this board contains a valid queen configuration or not
     */
    fun isValid() = getQueens().none { queen -> hasConflicts(queen) }

    /**
     * This method returns all the possible moves (valid or not) to generate new boards.
     *
     * Any board representable by this class MUST be obtainable by subsequent calls to getPossibleMoves().map { withQueen(it) }
     *
     * @return all the possible moves
     */
    open fun getPossibleMoves() = generateSequence(Queen(0, 0)) { prev ->
        when {
            prev.col < size - 1 -> Queen(prev.row, prev.col + 1)
            prev.row < size - 1 -> Queen(prev.row + 1, 0)
            else -> null
        }
    }.filterNot { q -> q in getQueens() }

    //TODO: use merge search?
    /**
     * @return whether a particular queen has conflicts
     */
    fun hasConflicts(queen: Queen) = getQueens().any { q2 -> q2 != queen && q2.conflicts(queen) }

    /**
     * This method filters the result of getPossibleMoves(), by removing invalid queen combinations
     *
     * @return all the possible moves
     */
    fun getValidMoves() = getPossibleMoves().filterNot { q -> hasConflicts(q) }

    /**
     * This method generates all the valid boards obtained by adding the queens of the method getValidMoves()
     *
     * @return all the valid boards obtainable by adding a queen
     */
    fun getNeighbors() = getValidMoves().map { q -> withQueen(q) }

    /**
     * This method prints the board
     */
    fun print() {
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (getQueens().contains(Queen(r, c))) {
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