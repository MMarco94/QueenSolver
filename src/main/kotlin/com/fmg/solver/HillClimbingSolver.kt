package com.fmg.solver

import com.fmg.Evaluator
import com.fmg.data.Board
import com.fmg.data.FullBoard

class HillClimbingSolver(size: Int, evaluator: Evaluator) :
    LocalOptimizationSolver(size, evaluator) {

    override fun createApproximationSequence(): Sequence<Board> {
        return generateSequence<Board>(FullBoard(size)) { prev ->
            prev.getNeighbors()
                .minBy { neighbor -> evaluator.evaluate(neighbor) }
        }
    }
}