package com.fmg

import kotlin.math.abs

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

    fun getValidMoves() = getPossibleMoves().filter { q ->
        getQueens().none { q2 -> q2.conflicts(q) }
    }

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

/**
 * This class is the basic implementation of Board, which can represent all possible boards
 */
class FullBoard(
    size: Int,
    private val queens: Set<Queen> = emptySet()
) : Board(size) {

    override fun withQueen(queen: Queen) = FullBoard(size, queens + queen)
    override fun withoutQueen(queen: Queen) = FullBoard(size, queens - queen)
    override fun getQueens() = queens
}

/**
 * This is a board that contains at most one queen per row,
 * and the rows are filled from the first to the last.
 *
 * This class cannot represent all possible boards, but can represent all valid boards
 */
class RowByRowBoard private constructor(
    size: Int,
    private val queensByRow: Map<Int, Queen>
) : Board(size) {

    val firstEmptyRow: Int? = if (queensByRow.size < size) {
        queensByRow.size
    } else {
        null
    }

    constructor(size: Int) : this(size, emptyMap())

    override fun getQueens() = queensByRow.values
    override fun withQueen(queen: Queen): RowByRowBoard {
        if (queen.row != firstEmptyRow) {
            throw IllegalArgumentException("Queens must be inserted sequentially")
        }
        return RowByRowBoard(size, queensByRow + (queen.row to queen))
    }

    override fun withoutQueen(queen: Queen) = RowByRowBoard(size, queensByRow.filterValues { it == queen })

    override fun getPossibleMoves(): Sequence<Queen> {
        return if (firstEmptyRow == null) {
            emptySequence()
        } else {
            (0 until size).asSequence()
                .map { col ->
                    Queen(firstEmptyRow, col)
                }
        }
    }
}

data class Queen(
    val row: Int,
    val col: Int
) {
    fun conflicts(another: Queen): Boolean {
        return row == another.row || col == another.col || abs(row - another.row) == abs(col - another.col)
    }
}