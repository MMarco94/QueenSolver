package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.data.BoardEvaluator
import com.fmg.data.BoardGenerator
import com.fmg.data.BoardWithScore
import com.fmg.data.NeighborsGenerator
import com.fmg.takeWhileInclusive
import kotlin.math.exp
import kotlin.math.log10

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
            neighborsGenerator.generateNeighbors(prevBoard.boardWithScore.board)

                .map { b -> b.withScore(evaluator) }
                .sortedBy { it.score }
                .take(100)

                .toList()
                .random(RANDOM)
                .let { currentBoard ->
                    val deltaScore = prevBoard.boardWithScore.score - currentBoard.score
                    val prob = acceptanceProb(deltaScore, tZero, prevBoard.stepsFromRennealing.toDouble())
                    if (RANDOM.nextDouble() < prob) {
                        currentBoard.withAnnealing(prevBoard.stepsFromRennealing + 1)
                    } else {
                        prevBoard.boardWithScore.withAnnealing()
                    }
                }
        }.takeWhileInclusive { (b) -> !b.board.isNQueenSolution() }
    }


    private fun acceptanceProb(deltaScore: Double, tZero: Double, k: Double): Double {
        return if (deltaScore >= 0) {
            1.0
        } else {
            exp(deltaScore * 50 / temperature(tZero, k))
        }
    }

    private fun temperature(tZero: Double, k: Double): Double {
        return tZero / log10(k)
    }
}

fun BoardWithScore.withAnnealing(annealingSteps: Int = 0) =
    BoardWithScoreAndAnnealing(BoardWithScore(board, score), annealingSteps);


data class BoardWithScoreAndAnnealing(val boardWithScore: BoardWithScore, val stepsFromRennealing: Int = 0)