package com.fmg.data

/**
 * This class is the basic implementation of Board, which can represent all possible boards
 */
class ValidBoard private constructor(
    size: Int,
    queens: Set<Queen>
) : Board(size, queens) {

    constructor(size: Int) : this(size, emptySet())

    override fun isValid() = true

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