package com.fmg.solver

import com.fmg.RANDOM
import com.fmg.allMinBy
import com.fmg.data.*
import com.fmg.terminate

class TabuSerachSolver(
    evaluator: BoardEvaluator,
    neighborsGenerator: NeighborsGenerator,
    boardGenerator: BoardGenerator,
    val localSearchTerminator: LocalSearchTerminator = TerminateWhenNotImproving
) : OptimizationSolver(evaluator, neighborsGenerator, boardGenerator) {

    var tabuList : MutableList<Board> = arrayListOf()

    override fun createApproximationSequenceWithScore(size: Int): Sequence<BoardWithScore> {
        return generateSequence(boardGenerator.generateBoard(size).withScore(evaluator)) { boardWithScore ->
            val nextBoard: BoardWithScore = neighborsGenerator.generateNeighbors(boardWithScore.board)
                .filter { b -> b !in tabuList }
                .map { b -> b.withScore(evaluator) }
                .allMinBy { board -> board.score }
                .random(RANDOM)

            tabuList.add(nextBoard.board)
            nextBoard


        }.terminate(localSearchTerminator)
    }
}