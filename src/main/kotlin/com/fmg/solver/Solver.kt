package com.fmg.solver

import com.fmg.data.Board
import com.fmg.data.BoardGenerator
import com.fmg.data.Evaluator
import com.fmg.data.NeighborsGenerator

abstract class Solver(val size: Int) {
    fun solve(): Board {
        val solution = createApproximationSequence().lastOrNull()
        if (solution == null || !solution.isNQueenSolution()) {
            throw UnableToSolveException("No solutions using the solver ${this::class.java.name}")
        }
        return solution
    }

    abstract fun createApproximationSequence(): Sequence<Board>
}

abstract class GraphSearchSolver(size: Int) : Solver(size) {

}

abstract class LocalOptimizationSolver(
    size: Int,
    val evaluator: Evaluator,
    val neighborsGenerator: NeighborsGenerator,
    val boardGenerator: BoardGenerator
) : Solver(size) {

}

abstract class GlobalOptimizationSolver(size: Int, val evaluator: Evaluator) : Solver(size) {

    abstract fun doStep()
}

class UnableToSolveException(message: String?, cause: Throwable? = null) : Exception(message, cause)