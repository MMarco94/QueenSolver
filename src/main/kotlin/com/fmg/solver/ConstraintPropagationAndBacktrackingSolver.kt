package com.fmg.solver

import com.fmg.data.Board
import com.fmg.data.NeighborsGenerator
import com.fmg.data.RowByRowNeighborsGenerator
import com.fmg.shuffled
import com.fmg.takeWhileInclusive

class ConstraintPropagationAndBacktrackingSolver(
    size: Int,
    val neighborsGenerator: NeighborsGenerator = RowByRowNeighborsGenerator
) : GraphSearchSolver(size) {

    override fun createApproximationSequence() = solve(Board(size))
        .takeWhileInclusive {
            !it.isNQueenSolution()
        }

    private fun solve(board: Board): Sequence<Board> {
        return neighborsGenerator.generateNeighbors(board)
            .filter { b -> b.isValid() }
            .shuffled()
            .flatMap { newBoard ->
                sequenceOf(newBoard) + solve(newBoard)
            }
    }
}