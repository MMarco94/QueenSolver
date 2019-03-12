package com.fmg.data

interface QueenEvaluator {
    fun evaluate(queen: Queen, context: Board): Double
}

object TotalQueenConflictEvaluator : QueenEvaluator {

    override fun evaluate(queen: Queen, context: Board): Double {
        return (context.queensDisposition.numberOfQueensOnColumn(queen.col) - 1 +
                context.queensDisposition.numberOfQueensOnRow(queen.row) - 1 +
                context.queensDisposition.numberOfQueensOnAscendingDiagonal(queen.ascendingDiagonalId) - 1 +
                context.queensDisposition.numberOfQueensOnDescendingDiagonal(queen.descendingDiagonalId) - 1)
            .toDouble()
    }
}