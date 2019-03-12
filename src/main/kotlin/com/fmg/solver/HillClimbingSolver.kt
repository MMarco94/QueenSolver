package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.allMinBy
import com.fmg.data.*
import com.fmg.terminate

class HillClimbingSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator,
    localSearchTerminator: LocalSearchTerminator = TerminateComposition(setOf(TerminateWhenSolved, TerminateWhenNotImproving))
) : LocalOptimizationSolver(evaluator, neighborsGenerator, boardGenerator, localSearchTerminator) {

    override fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore> {
        return generateSequence(boardGenerator.generateBoard(size).withScore(evaluator)) { (previousBoard, _) ->
            neighborsGenerator.generateNeighbors(previousBoard)
                .map { b -> b.withScore(evaluator) }
                .allMinBy { (_, weight) -> weight }
                .random(RANDOM)
        }.terminate(localSearchTerminator)
    }
}