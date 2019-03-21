package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.allMinBy
import com.fmg.data.*
import com.fmg.select
import com.fmg.takeWhileInclusive
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow

class SimulatedAnnealingSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator,
    val selector: LocalSearchSelector
) : OptimizationSolver(evaluator, neighborsGenerator, boardGenerator) {

    val tZero: Double = 1.0;

    val cooledStatusTemperature: Double = 0.1;

    override fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore> {
        return generateSequence( Pair(boardGenerator.generateBoard(size).withScore(evaluator),1) ) { (currentBoard,k) ->

            neighborsGenerator.generateNeighbors(currentBoard.board)
                .map { b -> b.withScore(evaluator) }
                .select(selector)
                .let { nextBoard ->

                    val deltaEnergy = nextBoard.score - currentBoard.score
                    val acceptanceProb = acceptanceProb(deltaEnergy, k)

                    if (RANDOM.nextDouble() < acceptanceProb) {
                        Pair(nextBoard, k + 1)
                    } else {
                        Pair(currentBoard, k + 1)
                    }
                }
        }.takeWhileInclusive { (b, k) ->
                    !b.board.isNQueenSolution() && temperature(k) > cooledStatusTemperature
        }.map { (b,_) -> b }

    }

    private fun acceptanceProb(deltaEnergy: Double, k: Int): Double {
        return if (deltaEnergy <= 0) {
            1.0
        } else {
            exp(-deltaEnergy / temperature(k))
        }
    }

    private fun temperature( k: Int): Double {
        return tZero / ln((k + 1).toDouble())
    }


}