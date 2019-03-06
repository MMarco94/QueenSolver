package com.fmg.solver

import com.fmg.data.Board
import com.fmg.shuffled
import com.fmg.takeWhileInclusive

class ConstraintPropagationAndBacktrackingSolver(
    initialBoard: Board,
    targetQueens: Int
) : GraphSearchSolver(initialBoard, targetQueens) {

    override fun createSolveSteps() = solve(initialBoard)

    private fun solve(board: Board): Sequence<Board> {
        return board.getNeighbors()
            .shuffled() //TODO: use better heuristic?
            .flatMap { newBoard ->
                sequenceOf(newBoard) + solve(newBoard)
            }
            .takeWhileInclusive {
                it.getQueens().size < targetQueens
            }
    }
}