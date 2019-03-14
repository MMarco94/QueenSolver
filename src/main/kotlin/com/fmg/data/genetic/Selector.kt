package com.fmg.data.genetic

import com.fmg.data.Board
import com.fmg.data.BoardEvaluator
import com.fmg.data.TotalConflictEvaluator
import kotlin.math.sqrt

interface Selector {
    fun select(population: Collection<Board>, boardLimit: Int = population.size): Collection<Board>
}

class FitnessSelector(
    private val fitnessFunction: BoardEvaluator = TotalConflictEvaluator
) : Selector {
    override fun select(population: Collection<Board>, boardLimit : Int) : Collection<Board> {
        var bl = boardLimit
        if (bl % 2 != 0) {
            bl += 1
        }
        return population.sortedBy { board -> fitnessFunction.evaluate(board) }.take(bl)
    }
}

// sqrt(population.first().size.toDouble()).toInt()