package com.fmg.solver

import com.fmg.Evaluator
import com.fmg.data.Board
import com.fmg.data.BoardGenerator
import com.fmg.data.NeighborsGenerator
import com.fmg.data.RandomBoardGenerator
import com.fmg.takeWhileInclusive

class HillClimbingSolver(
    size: Int,
    evaluator: Evaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator = RandomBoardGenerator
) : LocalOptimizationSolver(size, evaluator, neighborsGenerator, boardGenerator) {

    override fun createApproximationSequence(): Sequence<Board> {
        return generateSequence(boardGenerator.generateBoard(size)) { previousBoard ->
            neighborsGenerator.generateNeighbors(previousBoard)
                .groupBy { neighbor -> evaluator.evaluate(neighbor) }
                .minBy { (cost, _) -> cost }!!
                .value.random()
        }.takeWhileInclusive {
            !it.isNQueenSolution()
        }
    }
}