package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.data.BoardEvaluator
import com.fmg.data.TotalConflictEvaluator

interface Selector {
    fun select(population: Collection<Board>, boardLimit: Int = population.size / 4): Collection<Board>
}

class FitnessSelector(
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

class RouletteWheelSelector(
    private val fitnessFunction: BoardEvaluator = TotalConflictEvaluator
) : Selector {
    override fun select(population: Collection<Board>, boardLimit: Int): Collection<Board> {
        var bl = boardLimit
        if (bl % 2 != 0) {
            bl += 1
        }

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
        var value: Double
        for (i in 0 until weights.size) {
            weightSum += weights[i]
        }
        value = RANDOM.nextDouble(1.0) * weightSum
        var i = 0
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

class FitnessProportionalSelector(
    private val fitnessFunction: BoardEvaluator = TotalConflictEvaluator
) : Selector {
    override fun select(population: Collection<Board>, boardLimit: Int): Collection<Board> {
        val maxconflict = population.first().size * (population.first().size - 1) / 2.0
        var totalFitnessValue : Double

        var bl = boardLimit
        if (bl % 2 != 0) {
            bl += 1
        }

        val weights = mutableListOf<Double>()
        val populationSelected = mutableListOf<Board>()
        var selectedIndex: Int

        for (board in population) {
            weights.add(fitnessFunction.evaluate(board) / 1)
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
        var value: Double
        for (i in 0 until weights.size) {
            weightSum += weights[i]
        }
        value = RANDOM.nextDouble(1.0) * weightSum
        var i = 0
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