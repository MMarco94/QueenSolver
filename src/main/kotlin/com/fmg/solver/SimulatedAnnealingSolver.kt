package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.data.*
import com.fmg.shuffled
import com.fmg.takeWhileInclusive
import com.fmg.takeWithProbability
import kotlin.math.exp
import kotlin.math.log10
import kotlin.math.sqrt

class SimulatedAnnealingSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator
) : OptimizationSolver(evaluator, neighborsGenerator, boardGenerator) {

    override fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore> {
        return solve(size, 5.0)
            .map { b -> b.boardWithScore }
    }

    private fun solve(size: Int, tZero: Double): Sequence<BoardWithScoreAndAnnealing> {
        return generateSequence(boardGenerator.generateBoard(size).withScore(evaluator).withAnnealing()) { prevBoard ->
            val neighbors = neighborsGenerator.generateNeighbors(prevBoard.boardWithScore.board)
                .map { b -> b.withScore(evaluator) }
                //.sortedBy { it.score }

            (neighbors
                .takeWithProbability { boardWithScore -> exp(-(boardWithScore.score/ size.toDouble())) }
                .sortedBy { it.score }
                .firstOrNull()?:neighbors.toList().random())
                .let { currentBoard ->
                    val deltaScore = prevBoard.boardWithScore.score - currentBoard.score
                    val prob = acceptanceProb(deltaScore, tZero, prevBoard.stepsFromRennealing.toDouble())
                    if (RANDOM.nextDouble() < prob)
                        currentBoard.withAnnealing(prevBoard.stepsFromRennealing + 1)
                    else
                        prevBoard.boardWithScore.withAnnealing()
                }
        }.takeWhileInclusive { (b) -> !b.board.isNQueenSolution() }
    }


    private fun acceptanceProb(deltaScore: Double, tZero: Double, k: Double): Double {
        if (deltaScore >= 0)
            return 1.0
        else
            return exp(deltaScore * 50 / temperature(tZero, k))
    }

    private fun temperature(tZero: Double, k: Double): Double {
        return tZero / log10(k)
    }
    
}