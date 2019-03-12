package com.fmg.solver

import com.fmg.data.Board
import com.fmg.data.NeighborsGenerator
import com.fmg.data.RowByRowNeighborsGenerator
import com.fmg.shuffled
import com.fmg.takeWhileInclusive

class ConstraintPropagationAndBacktrackingSolver(
    val neighborsGenerator: NeighborsGenerator = RowByRowNeighborsGenerator
) : Solver() {

    override fun createApproximationSequence(size: Int) = solve(Board(size))
        .takeWhileInclusive {
            !it.isNQueenSolution()
        }

    private fun solve(board: Board): Sequence<Board> {
        return neighborsGenerator.generateNeighbors(board)
            .filterNot { b -> b.queensDisposition.hasConflicts() }
            .shuffled()
            .flatMap { newBoard ->
                sequenceOf(newBoard) + solve(newBoard)
            }
    }
}