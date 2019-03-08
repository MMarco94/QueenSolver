package com.fmg.data

import java.lang.Integer.max

interface Evaluator {
    fun evaluate(board: Board): Double
}

object TotalConflictEvaluator : Evaluator {

    override fun evaluate(board: Board): Double {
        return board.queenDisposition.countConflicts().toDouble()
    }
}

object ConflictEvaluator : Evaluator {

    override fun evaluate(board: Board): Double {
        return board.queens.count { queen ->
            board.queenDisposition.hasConflicts(queen)
        }.toDouble()
    }
}

object ConflictFreeEvaluator : Evaluator {
    override fun evaluate(board: Board): Double {
        return -Board.generateAllQueens(board.size)
            .filterNot { q -> q in board.queens }
            .count { q -> board.queenDisposition.hasConflicts(q) }
            .toDouble()

    }
}

// https://arxiv.org/ftp/arxiv/papers/1802/1802.02006.pdf
object FreeLinesEvaluator : Evaluator {
    override fun evaluate(board: Board): Double {
        val diagonalConflicts = (-board.size + 1 until board.size).asSequence()
            .sumBy { diagonalId ->
                max(0, board.queens.count { q -> (q.row - q.col) == diagonalId } - 1) +
                        max(0, board.queens.count { q -> (q.row + q.col - board.size + 1) == diagonalId } - 1)
            }
        val rowConflicts = (0 until board.size).asSequence()
            .sumBy { rowId ->
                max(0, board.queens.count { q -> q.row == rowId } - 1)
            }
        val columnConflicts = (0 until board.size).asSequence()
            .sumBy { columnId ->
                max(0, board.queens.count { q -> q.col == columnId } - 1)
            }

        return (diagonalConflicts + rowConflicts + columnConflicts).toDouble()

    }
}

class SumEvaluator(val evaluators: List<Evaluator>) : Evaluator {
    override fun evaluate(board: Board): Double {
        return evaluators.sumByDouble { e -> e.evaluate(board) }
    }
}