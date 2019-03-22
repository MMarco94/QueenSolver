package com.fmg.solver

import com.fmg.allMinBy
import com.fmg.data.*
import com.fmg.randomOrNull
import com.fmg.terminate

class HillClimbingSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator,
    val localSearchTerminator: LocalSearchTerminator = TerminateWhenNotImproving
) : OptimizationSolver(evaluator, neighborsGenerator, boardGenerator) {

    override fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore> {
        val initialBoard = boardGenerator.generateBoard(size).withScore(evaluator)
        return generateSequence(initialBoard) { previousBoard ->
            neighborsGenerator.generateNeighbors(previousBoard.board)
                .map { b -> b.withScore(evaluator) }
                .allMinBy { board -> board.score }
                .randomOrNull()//Choosing one random neighbor that minimizes the evaluator
        }.terminate(localSearchTerminator)
    }
}