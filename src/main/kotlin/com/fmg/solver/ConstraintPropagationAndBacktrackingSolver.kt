package com.fmg.solver

import com.fmg.data.Board
import com.fmg.data.ValidBoard
import com.fmg.shuffled
import com.fmg.takeWhileInclusive

class ConstraintPropagationAndBacktrackingSolver(
    size: Int
) : GraphSearchSolver(size) {

    override fun createApproximationSequence() = solve(ValidBoard(size))
        .takeWhileInclusive {
            !it.isNQueenSolution()
        }

    private fun solve(board: Board): Sequence<Board> {
        return board
            .getNeighbors()
            .shuffled()
            .flatMap { newBoard ->
                sequenceOf(newBoard) + solve(newBoard)
            }
    }
}