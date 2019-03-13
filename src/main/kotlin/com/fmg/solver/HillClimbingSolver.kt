package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.allMinBy
import com.fmg.data.*
import com.fmg.terminate

class HillClimbingSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator,
    val localSearchTerminator: LocalSearchTerminator = TerminateWhenNotImproving
) : OptimizationSolver(evaluator, neighborsGenerator, boardGenerator) {
    
    override fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore> {
        return generateSequence(boardGenerator.generateBoard(size).withScore(evaluator)) { boardWithScore ->
            neighborsGenerator.generateNeighbors(boardWithScore.board)
                .map { b -> b.withScore(evaluator) }
                .allMinBy { board -> board.score }
                .random(RANDOM)
        }.terminate(localSearchTerminator)
    }
}