package com.fmg.solver

import com.fmg.Evaluator
import com.fmg.data.Board
import com.fmg.data.FullBoard
import com.fmg.takeWhileInclusive

class HillClimbingSolver(size: Int, evaluator: Evaluator) :
    LocalOptimizationSolver(size, evaluator) {

    override fun createApproximationSequence(): Sequence<Board> {
        return generateSequence(FullBoard(size)) { previousBoard ->
            previousBoard.getNeighbors()
                .groupBy { neighbor -> evaluator.evaluate(neighbor) }
                .minBy { (cost, _) -> cost }!!
                .value.random()
        }.takeWhileInclusive {
            !it.isNQueenSolution()
        }
    }
}