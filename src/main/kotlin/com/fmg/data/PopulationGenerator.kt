package com.fmg.data

interface PopulationGenerator {
    fun generatePopulation(): Collection<Board>
}

class IndependentPopulationGenerator(val boardSize: Int, val boardGenerator: BoardGenerator, val populationSize: Int) :
    PopulationGenerator {
    override fun generatePopulation(): Collection<Board> {
        val population = mutableListOf<Board>()
        for (i in 0 until populationSize) {
            population.add(boardGenerator.generateBoard(boardSize))
        }

        return population
    }
}