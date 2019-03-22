package com.fmg.data.genetic

import com.fmg.data.Board
import com.fmg.data.BoardGenerator
import com.fmg.data.OneQueenPerRowAndColumnRandomBoardGenerator
import kotlin.math.sqrt

interface PopulationGenerator {
    fun generatePopulation(boardSize: Int, populationSize: Int = 10 * boardSize): Collection<Board>
}

/**
 * This PopulationGenerator keeps the number of board generated and the way in which they are generated as separate concepts.
 */
class IndependentPopulationGenerator(private val boardGenerator: BoardGenerator = OneQueenPerRowAndColumnRandomBoardGenerator) :
    PopulationGenerator {
    override fun generatePopulation(boardSize: Int, populationSize: Int): Collection<Board> {
        var ps = populationSize
        val population = mutableListOf<Board>()
        if (ps % 2 != 0) {
            ps += 1
        }
        for (i in 0 until ps) {
            population.add(boardGenerator.generateBoard(boardSize))
        }

        return population
    }
}