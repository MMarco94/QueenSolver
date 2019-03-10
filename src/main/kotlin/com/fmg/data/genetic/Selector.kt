package com.fmg.data.genetic

import com.fmg.data.Board
import com.fmg.data.Evaluator

interface Selector {
    fun select(population: Collection<Board>): Collection<Board>
}

class FitnessSelector(
    val boardLimit: Int,
    val fitnessFunction: Evaluator
) : Selector {
    override fun select(population: Collection<Board>) : Collection<Board> {
        return population.sortedBy { board -> fitnessFunction.evaluate(board) }.take(boardLimit)
    }
}