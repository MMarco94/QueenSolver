package com.fmg.data

import java.lang.Math.abs

interface QueenEvaluator {
    fun evaluate(queen:Queen, context: Board): Double
}

object TotalQueenConflictEvaluator : QueenEvaluator {

    override fun evaluate(queen:Queen, context: Board): Double {
        // TODO optimize with AND operator instead of OR
        return -context.queens
            .asSequence()
            .filter { q -> q.col == queen.col || abs(q.row - queen.row) == abs(q.col - queen.col)}
            .count()
            .toDouble()
    }
}