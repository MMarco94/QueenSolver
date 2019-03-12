package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.allMinBy
import com.fmg.data.*
import com.fmg.takeWhileInclusive

class HillClimbingSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator = RandomBoardGenerator
) : LocalOptimizationSolver(evaluator, neighborsGenerator, boardGenerator) {

    override fun createApproximationSequence(size: Int): Sequence<Board> {
        return generateSequence(boardGenerator.generateBoard(size)) { previousBoard ->
            neighborsGenerator.generateNeighbors(previousBoard)
                .allMinBy { board ->
                    evaluator.evaluate(board)
                }
                .random(RANDOM)
        }.takeWhileInclusive {
            !it.isNQueenSolution()
        }
    }
}