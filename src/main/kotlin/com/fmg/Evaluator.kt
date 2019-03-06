package com.fmg

import com.fmg.data.UnconstrainedBoard

interface Evaluator {
    fun evaluate(board: UnconstrainedBoard): Double
}

class TotalConflictEvaluator : Evaluator {

    override fun evaluate(board: UnconstrainedBoard): Double {
        return board.queens.sumBy { queen ->
            board.queens.count { q -> queen != q && queen.conflicts(q) }
        }.toDouble()
    }
}

class ConflictEvaluator : Evaluator {

    override fun evaluate(board: UnconstrainedBoard): Double {
        return board.queens.count { queen ->
            board.queens.any { q -> queen != q && queen.conflicts(q) }
        }.toDouble()
    }
}