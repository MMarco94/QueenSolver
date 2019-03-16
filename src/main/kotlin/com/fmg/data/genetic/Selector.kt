package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.data.BoardEvaluator
import com.fmg.data.TotalConflictEvaluator

interface Selector {
    fun select(population: Collection<Board>, boardLimit: Int = population.size / 2): Collection<Board>
}

class StaticFitnessSelector(
    private val fitnessFunction: BoardEvaluator = TotalConflictEvaluator
) : Selector {
    override fun select(population: Collection<Board>, boardLimit: Int): Collection<Board> {
        var bl = boardLimit
        if (bl % 2 != 0) {
            bl += 1
        }
        return population.sortedBy { board -> fitnessFunction.evaluate(board) }.take(bl)
    }
}

@Suppress("NAME_SHADOWING")
class RouletteWheelSelector(
    private val fitnessFunction: BoardEvaluator = TotalConflictEvaluator
) : Selector {
    override fun select(population: Collection<Board>, boardLimit: Int): Collection<Board> {
        val weights = mutableListOf<Double>()
        val populationSelected = mutableListOf<Board>()
        var selectedIndex: Int

        for (board in population) {
            weights.add(1.0 / fitnessFunction.evaluate(board))
        }

        for (i in 0 until boardLimit) {
            selectedIndex = rouletteSelect(weights)
            //println(selectedIndex)
            val board = population.toMutableList().removeAt(selectedIndex)
            weights.removeAt(selectedIndex)
            populationSelected.add(board)
        }

        return populationSelected
    }

    private fun rouletteSelect(weights: List<Double>): Int {
        var weightSum = 0.0
        var i = 0
        var value: Double
        for (i in 0 until weights.size) {
            weightSum += weights[i]
        }
        value = RANDOM.nextDouble(1.0) * weightSum

        while (value > 0.0 && i < weights.size) {
            value -= weights[i]
            i++
        }
        return if (value < 0) {
            i - 1
        } else {
            weights.size - 1
        }
    }
}

// sqrt(population.first().size.toDouble()).toInt()