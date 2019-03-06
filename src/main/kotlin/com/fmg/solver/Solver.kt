package com.fmg.solver

import com.fmg.data.UnconstrainedBoard
import com.fmg.Evaluator

abstract class Solver(val initialBoard: UnconstrainedBoard, val targetQueens: Int) {
    abstract fun solve(): UnconstrainedBoard
}

abstract class GraphSearchSolver(initialBoard: UnconstrainedBoard, targetQueens: Int) : Solver(initialBoard, targetQueens) {

    override fun solve(): UnconstrainedBoard {
        return createSolveSteps().firstOrNull() ?: throw UnableToSolveException("No solution found!")
    }

    abstract fun createSolveSteps(): Sequence<UnconstrainedBoard>
}

abstract class LocalOptimizationSolver(initialBoard: UnconstrainedBoard, targetQueens: Int, val evaluator: Evaluator) :
    Solver(initialBoard, targetQueens) {

    abstract fun doStep()
}

abstract class GlobalOptimizationSolver(initialBoard: UnconstrainedBoard, targetQueens: Int) : Solver(initialBoard, targetQueens) {

    abstract fun doStep()
}

class UnableToSolveException(message: String?, cause: Throwable? = null) : Exception(message, cause)