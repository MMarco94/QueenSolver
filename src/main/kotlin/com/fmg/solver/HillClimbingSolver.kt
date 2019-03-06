package com.fmg.solver

import com.fmg.Evaluator
import com.fmg.data.Queen
import com.fmg.data.UnconstrainedBoard
import java.util.*

class HillClimbingSolver(initialBoard: UnconstrainedBoard, targetQueens: Int, evaluator: Evaluator) :
    LocalOptimizationSolver(initialBoard, targetQueens, evaluator) {
    override fun solve(): UnconstrainedBoard {
        val random = Random()
        var board = initialBoard
        while (board.queens.size < targetQueens) {
            board = initialBoard.withQueen(Queen(random.nextInt(board.size), random.nextInt(board.size)))
        }
        return solve(board)
    }

    fun solve(board: UnconstrainedBoard) : UnconstrainedBoard {
        return board
            .getNeighbors()
            .minBy { b -> evaluator.evaluate(board) }!!
    }

    override fun doStep() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}