package com.fmg.data

/**
 * A class that holds queens. It also keeps statistics about the dispositions of the queens on the board, to make most operations O(1)
 */
class QueensDisposition private constructor(
    val queens: FastQueenSet,
    private val rows: IntArray,//Row => Number of queens of that row
    private val columns: IntArray,
    private val descendingDiagonals: IntArray,
    private val ascendingDiagonals: IntArray,
    val conflictsCount: Int
) {

    constructor(maxBoardSize: Int) : this(
        FastQueenSet(),
        IntArray(maxBoardSize),
        IntArray(maxBoardSize),
        IntArray(maxBoardSize * 2 + 1),
        IntArray(maxBoardSize * 2 + 1),
        0
    )

    fun hasConflicts(): Boolean {
        if (conflictsCount != conflictsCount) {
            throw IllegalStateException("Asd = $conflictsCount; conflictsCount=$conflictsCount")
        }
        return conflictsCount > 0
    }

    fun countConflicts(queen: Queen): Int {
        var ret = 0
        ret += numberOfQueensOnRow(queen.row)
        ret += numberOfQueensOnColumn(queen.col)
        ret += numberOfQueensOnDescendingDiagonal(queen.descendingDiagonalId)
        ret += numberOfQueensOnAscendingDiagonal(queen.ascendingDiagonalId)
        if (queen in queens) {
            ret -= 4
        }
        return ret
    }

    fun hasConflicts(queen: Queen): Boolean = countConflicts(queen) > 0

    fun numberOfQueensOnRow(row: Int) = rows[row]
    fun hasQueensOnRow(row: Int) = numberOfQueensOnRow(row) > 0

    fun numberOfQueensOnColumn(column: Int) = columns[column]
    fun hasQueensOnColumn(column: Int) = numberOfQueensOnRow(column) > 0

    fun numberOfQueensOnDescendingDiagonal(descendingDiagonalId: Int) = descendingDiagonals[descendingDiagonalId]
    fun hasQueensOnDescendingDiagonal(descendingDiagonalId: Int) = numberOfQueensOnRow(descendingDiagonalId) > 0

    fun numberOfQueensOnAscendingDiagonal(ascendingDiagonalId: Int) = ascendingDiagonals[ascendingDiagonalId]
    fun hasQueensOnAscendingDiagonal(ascendingDiagonalId: Int) = numberOfQueensOnRow(ascendingDiagonalId) > 0

    fun getConflictsCountOnRow(row: Int): Int {
        val r = numberOfQueensOnRow(row)
        return r * (r - 1) / 2
    }

    fun rowHasConflicts(row: Int) = numberOfQueensOnRow(row) > 0

    fun getConflictsCountOnColumn(column: Int): Int {
        val r = numberOfQueensOnColumn(column)
        return r * (r - 1) / 2
    }

    fun columnHasConflicts(column: Int) = numberOfQueensOnColumn(column) > 0

    fun withQueen(queen: Queen): QueensDisposition {
        return if (queen in queens) {
            this //This disposition already contains the given queen
        } else {
            QueensDisposition(
                queens.withQueen(queen),
                rows.copyOf().also { m ->
                    m[queen.row]++
                },
                columns.copyOf().also { m ->
                    m[queen.col]++
                },
                descendingDiagonals.copyOf().also { m ->
                    m[queen.descendingDiagonalId]++
                },
                ascendingDiagonals.copyOf().also { m ->
                    m[queen.ascendingDiagonalId]++
                },
                conflictsCount + numberOfQueensOnRow(queen.row) +
                        numberOfQueensOnColumn(queen.col) +
                        numberOfQueensOnAscendingDiagonal(queen.ascendingDiagonalId) +
                        numberOfQueensOnDescendingDiagonal(queen.descendingDiagonalId)
            )
        }
    }

    fun withoutQueen(queen: Queen): QueensDisposition {
        if (queen !in queens) {
            throw IllegalArgumentException()
        }
        return QueensDisposition(
            queens.withoutQueen(queen),
            rows.copyOf().also { m ->
                m[queen.row]--
            },
            columns.copyOf().also { m ->
                m[queen.col]--
            },
            descendingDiagonals.copyOf().also { m ->
                m[queen.descendingDiagonalId]--
            },
            ascendingDiagonals.copyOf().also { m ->
                m[queen.ascendingDiagonalId]--
            },
            conflictsCount + 4 - numberOfQueensOnRow(queen.row) -
                    numberOfQueensOnColumn(queen.col) -
                    numberOfQueensOnAscendingDiagonal(queen.ascendingDiagonalId) -
                    numberOfQueensOnDescendingDiagonal(queen.descendingDiagonalId)

        )
    }
}