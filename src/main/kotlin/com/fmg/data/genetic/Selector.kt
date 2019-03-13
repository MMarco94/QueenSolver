package com.fmg.data.genetic

import com.fmg.data.Board
import com.fmg.data.BoardEvaluator
import kotlin.math.sqrt

interface Selector {
    fun select(population: Collection<Board>): Collection<Board>
}

class FitnessSelector(
    private val fitnessFunction: BoardEvaluator
) : Selector {
    override fun select(population: Collection<Board>) : Collection<Board> {
        var boardLimit = population.size * sqrt(population.size.toDouble()).toInt()
        if (boardLimit % 2 != 0) {
            boardLimit += 1
        }

        return population.sortedBy { board -> fitnessFunction.evaluate(board) }.take(boardLimit)
    }
}