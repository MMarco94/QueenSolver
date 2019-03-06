package com.fmg.solver

import com.fmg.Board
import com.fmg.Evaluator

abstract class Solver(val initialBoard: Board, val targetQueens: Int) {
    abstract fun solve(): Board
}

abstract class GraphSearchSolver(initialBoard: Board, targetQueens: Int) : Solver(initialBoard, targetQueens) {

    override fun solve(): Board {
        return createSolveSteps().firstOrNull() ?: throw UnableToSolveException("No solution found!")
    }

    abstract fun createSolveSteps(): Sequence<Board>
}

abstract class LocalOptimizationSolver(initialBoard: Board, targetQueens: Int, val evaluator: Evaluator) :
    Solver(initialBoard, targetQueens) {

    abstract fun doStep()
}

abstract class GlobalOptimizationSolver(initialBoard: Board, targetQueens: Int) : Solver(initialBoard, targetQueens) {

    abstract fun doStep()
}

class UnableToSolveException(message: String?, cause: Throwable? = null) : Exception(message, cause)