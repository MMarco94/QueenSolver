package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.data.BoardEvaluator
import com.fmg.data.BoardGenerator
import com.fmg.data.BoardWithScore
import com.fmg.data.NeighborsGenerator
import com.fmg.extractWithRepetitions
import com.fmg.takeWhileInclusive
import kotlin.math.exp

class SimulatedAnnealingSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator
) : OptimizationSolver(evaluator, neighborsGenerator, boardGenerator) {

    override fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore> {
        return generateSequence(boardGenerator.generateBoard(size).withScore(evaluator)) { (prevBoard, prevScore) ->
            neighborsGenerator.generateNeighbors(prevBoard)
                .extractWithRepetitions()
                .map { b -> b.withScore(evaluator) }
                .first { (_, score) ->
                    score < prevScore || RANDOM.nextDouble() < exp(-(score - prevScore) / prevScore) * 0.0001
                }
        }.takeWhileInclusive { (b) -> !b.isNQueenSolution() }
    }
}