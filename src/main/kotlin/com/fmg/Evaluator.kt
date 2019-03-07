package com.fmg

import com.fmg.data.Board

interface Evaluator {
    fun evaluate(board: Board): Double
}

object TotalConflictEvaluator : Evaluator {

    override fun evaluate(board: Board): Double {
        return board.queens.sumBy { queen ->
            board.queens.count { q -> queen != q && queen.conflicts(q) }
        }.toDouble()
    }
}

object ConflictEvaluator : Evaluator {

    override fun evaluate(board: Board): Double {
        return board.queens.count { queen ->
            board.queens.any { q -> queen != q && queen.conflicts(q) }
        }.toDouble()
    }
}