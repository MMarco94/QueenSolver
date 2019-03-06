package com.fmg.solver

import com.fmg.data.UnconstrainedBoard
import com.fmg.shuffled
import com.fmg.takeWhileInclusive

class ConstraintPropagationAndBacktrackingSolver(
    initialBoard: UnconstrainedBoard,
    targetQueens: Int
) : GraphSearchSolver(initialBoard, targetQueens) {

    override fun createSolveSteps() = solve(initialBoard)

    private fun solve(board: UnconstrainedBoard): Sequence<UnconstrainedBoard> {
        return board.getNeighbors()
            .shuffled() //TODO: use better heuristic?
            .flatMap { newBoard ->
                sequenceOf(newBoard) + solve(newBoard)
            }
            .takeWhileInclusive {
                it.queens.size < targetQueens
            }
    }
}