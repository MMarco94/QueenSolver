package com.fmg.solver

import com.fmg.Board
import com.fmg.takeWhileInclusive

class ConstraintPropagationAndBacktrackingSolver(initialBoard: Board, targetQueens: Int) :

    GraphSearchSolver(initialBoard, targetQueens) {
    override fun createSolveSteps() = solve(initialBoard)

    private fun solve(board: Board): Sequence<Board> {
        return board.getValidMoves()
            .map { board.withQueen(it) }
            .flatMap { newBoard ->
                sequenceOf(newBoard) + solve(newBoard)
            }
            .takeWhileInclusive {
                it.queens.size < targetQueens
            }
    }
}