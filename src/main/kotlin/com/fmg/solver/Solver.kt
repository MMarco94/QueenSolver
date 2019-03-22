package com.fmg.solver

import com.fmg.data.*

abstract class Solver {
    fun solve(size: Int): Board {
        val solution = createApproximationSequence(size).lastOrNull()
        if (solution == null || !solution.isNQueenSolution()) {
            throw UnableToSolveException("No solutions using the solver ${this::class.java.name}")
        }
        return solution
    }

    abstract fun createApproximationSequence(size: Int): Sequence<Board>
}

abstract class OptimizationSolver(
    val evaluator: BoardEvaluator,
    val neighborsGenerator: NeighborsGenerator,
    val boardGenerator: BoardGenerator
) : Solver() {

    override fun createApproximationSequence(size: Int) = createApproximationSequenceWithScore(size).map { it.board }
    abstract fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore>

}

class UnableToSolveException(message: String?, cause: Throwable? = null) : Exception(message, cause)