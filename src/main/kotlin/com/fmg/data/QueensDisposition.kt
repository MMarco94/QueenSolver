package com.fmg.data

/**
 * A class that holds queens. It also keeps statistics about the dispositions of the queens on the board, to make most operations O(1)
 */
class QueensDisposition private constructor(
    val queensObtainer: () -> FastQueenSet,
    private val rows: IntArray,//Row => Number of queens of that row
    private val columns: IntArray,
    private val descendingDiagonals: IntArray,
    private val ascendingDiagonals: IntArray,
    val conflictsCount: Int
) {

    val queens: FastQueenSet by lazy {
        queensObtainer()
    }

    constructor(maxBoardSize: Int) : this(
        { FastQueenSet() },
        IntArray(maxBoardSize),
        IntArray(maxBoardSize),
        IntArray(maxBoardSize * 2 + 1),
        IntArray(maxBoardSize * 2 + 1),
        0
    )

    fun hasConflicts(): Boolean {
        return conflictsCount > 0
    }

    fun countConflicts(queen: Queen): Int {
        var ret = 0
        ret += numberOfQueensOnRow(queen.row)
        ret += numberOfQueensOnColumn(queen.col)
        ret += numberOfQueensOnDescendingDiagonal(queen.descendingDiagonalId)
        ret += numberOfQueensOnAscendingDiagonal(queen.ascendingDiagonalId)
        ret -= 4 * queens.size
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

    fun rowHasConflicts(row: Int) = numberOfQueensOnRow(row) > 0

    fun columnHasConflicts(column: Int) = numberOfQueensOnColumn(column) > 0

    fun withQueen(queen: Queen): QueensDisposition {
        return with(toAddQueens = arrayOf(queen))
    }

    fun withoutQueen(queen: Queen): QueensDisposition {
        return with(toRemoveQueens = arrayOf(queen))
    }

    fun with(
        toAddQueens: Array<Queen> = emptyArray(),
        toRemoveQueens: Array<Queen> = emptyArray()
    ): QueensDisposition {
        val reallyNewQueens = toAddQueens.filter { it !in queens }.toTypedArray()

        val newRows = rows.copyOf()
        val newColumns = columns.copyOf()
        val newDescDiag = descendingDiagonals.copyOf()
        val newAscDiag = ascendingDiagonals.copyOf()
        var newConflictCount = conflictsCount
        for (q in reallyNewQueens) {
            newConflictCount += newRows[q.row] +
                    newColumns[q.col] +
                    newDescDiag[q.descendingDiagonalId] +
                    newAscDiag[q.ascendingDiagonalId]
            newRows[q.row]++
            newColumns[q.col]++
            newDescDiag[q.descendingDiagonalId]++
            newAscDiag[q.ascendingDiagonalId]++
        }
        for (q in toRemoveQueens) {
            if (q !in queens) {
                throw IllegalArgumentException()
            } else if (q in toAddQueens) {
                throw IllegalArgumentException()
            }
            newConflictCount -= -4 + newRows[q.row] +
                    newColumns[q.col] +
                    newDescDiag[q.descendingDiagonalId] +
                    newAscDiag[q.ascendingDiagonalId]
            newRows[q.row]--
            newColumns[q.col]--
            newDescDiag[q.descendingDiagonalId]--
            newAscDiag[q.ascendingDiagonalId]--
        }
        return QueensDisposition(
            { queens.with(toAddQueens = reallyNewQueens, toRemoveQueens = toRemoveQueens) },
            newRows,
            newColumns,
            newDescDiag,
            newAscDiag,
            newConflictCount
        )
    }
}