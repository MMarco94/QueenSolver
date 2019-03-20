package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.data.*
import com.fmg.takeWhileInclusive
import kotlin.math.exp
import kotlin.math.log10

class SimulatedAnnealingSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator
) : OptimizationSolver(evaluator, neighborsGenerator, boardGenerator) {

    val tZero: Double = 100.0;

    val cooledStatusTemperature: Double = 1.0;

    override fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore> {
        return generateSequence(boardGenerator.generateBoard(size).withScore(evaluator).withAnnealingSteps()) { currentBoard ->
            neighborsGenerator.generateNeighbors(currentBoard.boardWithScore.board)
                .map { b -> b.withScore(evaluator) }
                .toList()
                .random(RANDOM)
                .let { nextBoard ->
                    val deltaEnergy = nextBoard.score - currentBoard.boardWithScore.score
                    val prob = acceptanceProb(deltaEnergy, currentBoard.stepsFromRennealing.toDouble())
                    if (RANDOM.nextDouble() < prob) {
                        nextBoard.withAnnealingSteps(currentBoard.stepsFromRennealing + 1)
                    } else {
                        currentBoard.boardWithScore.withAnnealingSteps()
                    }
                }
        }.takeWhileInclusive { b ->
            val temp = temperature(b.stepsFromRennealing.toDouble())
            temp > cooledStatusTemperature }

            .map { b -> b.boardWithScore }

    }

    private fun acceptanceProb(deltaEnergy: Double, k: Double): Double {
        return if (deltaEnergy <= 0) {
            1.0
        } else {
            exp(-deltaEnergy * 20 / temperature(k))
        }
    }

    private fun temperature( k: Double): Double {
        return tZero / log10(k + 1)
    }
}

fun BoardWithScore.withAnnealingSteps(annealingSteps: Int = 1) =
    BoardWithScoreAndAnnealing(BoardWithScore(board, score), annealingSteps);


data class BoardWithScoreAndAnnealing(val boardWithScore: BoardWithScore, val stepsFromRennealing: Int = 0)