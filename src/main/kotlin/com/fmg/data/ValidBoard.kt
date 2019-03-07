package com.fmg.data

/**
 * This class represents a board. In this board, only valid solutions are representable
 */
class ValidBoard private constructor(
    size: Int,
    queens: Set<Queen>
) : Board(size, queens) {

    constructor(size: Int) : this(size, emptySet())

    override fun isValid() = true


    /**
     * index of first empty row. null if the board is complete
     */
    val firstEmptyRow: Int? = (0 until size).asSequence()
        .firstOrNull { row ->
            queens.none { q -> q.row == row }
        }

    override fun withQueen(queen: Queen): ValidBoard {
        if (queen.row != firstEmptyRow) {
            throw IllegalArgumentException("Cannot create a valid board with the given queen")
        }
        return ValidBoard(size, queens + queen)
    }

    override fun withoutQueen(queen: Queen) = ValidBoard(size, queens - queen)

    override fun getNeighbors(): Sequence<ValidBoard> {
        return if (firstEmptyRow == null) {
            emptySequence()
        } else {
            (0 until size).asSequence()
                .map { col -> Queen(firstEmptyRow, col) }
                .filterNot { q -> hasConflicts(q) }
                .map { q -> withQueen(q) }
        }
    }
}