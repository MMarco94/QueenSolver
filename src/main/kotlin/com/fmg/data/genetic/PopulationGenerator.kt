package com.fmg.data.genetic

import com.fmg.data.Board
import com.fmg.data.BoardGenerator

interface PopulationGenerator {
    fun generatePopulation(boardSize: Int): Collection<Board>
}

/**
 * This PopulationGenerator keeps the number of board generated and the way in which they are generated as separate concepts.
 */
class IndependentPopulationGenerator(private val boardGenerator: BoardGenerator) :
    PopulationGenerator {
    override fun generatePopulation(boardSize: Int): Collection<Board> {
        val population = mutableListOf<Board>()
        var populationSize = boardSize * boardSize
        if (populationSize % 2 != 0) {
            populationSize += 1
        }
        for (i in 0 until populationSize) {
            population.add(boardGenerator.generateBoard(boardSize))
        }

        return population
    }
}