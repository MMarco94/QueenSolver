package com.fmg.solver

import com.fmg.data.Board
import com.fmg.data.BoardGenerator
import com.fmg.data.BoardEvaluator
import com.fmg.data.NeighborsGenerator

abstract class Solver() {
    fun solve(size: Int): Board {
        val solution = createApproximationSequence(size).lastOrNull()
        if (solution == null || !solution.isNQueenSolution()) {
            throw UnableToSolveException("No solutions using the solver ${this::class.java.name}")
        }
        return solution
    }

    abstract fun createApproximationSequence(size: Int): Sequence<Board>
}

abstract class LocalOptimizationSolver(
    val evaluator: BoardEvaluator,
    val neighborsGenerator: NeighborsGenerator,
    val boardGenerator: BoardGenerator
) : Solver() {

}

class UnableToSolveException(message: String?, cause: Throwable? = null) : Exception(message, cause)