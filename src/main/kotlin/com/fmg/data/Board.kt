package com.fmg.data

/**
 * This class represents a chess board size x size.
 *
 * Any board representable by this class MUST be obtainable by subsequent calls to getNeighbors
 */
abstract class Board(
    val size: Int
) {

    abstract fun getQueens(): Collection<Queen>
    abstract fun withQueen(queen: Queen): Board
    abstract fun withoutQueen(queen: Queen): Board

    fun isValid(): Boolean {
        return getQueens().none { queen ->
            getQueens().any { q -> queen != q && queen.conflicts(q) }
        }
    }

    open fun getPossibleMoves() = generateSequence(Queen(0, 0)) { prev ->
        when {
            prev.col < size - 1 -> Queen(prev.row, prev.col + 1)
            prev.row < size - 1 -> Queen(prev.row + 1, 0)
            else -> null
        }
    }.filterNot { q -> q in getQueens() }

    fun conflicts(queen: Queen) = getQueens().any { q2 -> q2.conflicts(queen) }

    fun getValidMoves() = getPossibleMoves().filterNot { q -> conflicts(q) }


    fun getNeighbors() = getValidMoves().map { q -> withQueen(q) }

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