package com.fmg.data

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