package com.fmg.data

/**
 * This class is the basic implementation of Board, which can represent all possible boards
 */
class ConstraintBoard(
    size: Int, queens: Set<Queen> = emptySet()
) : UnconstrainedBoard(size, queens) {

    override fun getPossibleMoves(): Sequence<Queen> {
        val firstRow = (0 until size).asSequence()
            .firstOrNull { row ->
                queens.none { q -> q.row == row }
            }

        return if (firstRow == null) {
            emptySequence()
        } else {
            (0 until size).asSequence().map { col -> Queen(firstRow, col) }
        }
    }
}