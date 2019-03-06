package com.fmg

interface Evaluator {
    fun evaluate(board: Board): Double
}

class TotalConflictEvaluator : Evaluator {

    override fun evaluate(board: Board): Double {
        return board.getQueens().sumBy { queen ->
            board.getQueens().count { q -> queen != q && queen.conflicts(q) }
        }.toDouble()
    }
}

class ConflictEvaluator : Evaluator {

    override fun evaluate(board: Board): Double {
        return board.getQueens().count { queen ->
            board.getQueens().any { q -> queen != q && queen.conflicts(q) }
        }.toDouble()
    }
}