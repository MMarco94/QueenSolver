package com.fmg.solver

import com.fmg.Evaluator
import com.fmg.data.Board
import com.fmg.data.FullBoard

class HillClimbingSolver(size: Int, evaluator: Evaluator) :
    LocalOptimizationSolver(size, evaluator) {

    override fun createApproximationSequence(): Sequence<Board> {
        return createApproximationSequenceWithCost().map { it.second }
    }

    private fun createApproximationSequenceWithCost(): Sequence<Pair<Double, Board>> {
        val board: Board = FullBoard(size)
        return generateSequence(evaluator.evaluate(board) to board) { (previousCost, previousBoard) ->
            val (nextCost, nextBoards) = previousBoard.getNeighbors()
                .groupBy { neighbor -> evaluator.evaluate(neighbor) }
                .minBy { (cost, _) -> cost }!!

            val nextBoard = nextBoards.random()

            if (previousBoard.queens.size >= nextBoard.queens.size && previousCost <= nextCost) {
                null
            } else {
                nextCost to nextBoard
            }
        }
    }
}