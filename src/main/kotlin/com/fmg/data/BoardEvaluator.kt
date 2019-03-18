package com.fmg.data

interface BoardEvaluator {
    fun evaluate(board: Board): Double
}

object TotalConflictEvaluator : BoardEvaluator {

    override fun evaluate(board: Board): Double {
        return board.queensDisposition.conflictsCount.toDouble()
    }
}

object ConflictEvaluator : BoardEvaluator {

    override fun evaluate(board: Board): Double {
        return board.queens.count { queen ->
            board.queensDisposition.hasConflicts(queen)
        }.toDouble()
    }
}

object ConflictFreeEvaluator : BoardEvaluator {
    override fun evaluate(board: Board): Double {
        return Board.generateAllQueens(board.size)
            .filterNot { q -> q in board.queens }
            .count { q -> !board.queensDisposition.hasConflicts(q) }
            .toDouble()

    }
}
