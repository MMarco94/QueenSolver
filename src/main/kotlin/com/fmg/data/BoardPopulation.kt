package com.fmg.data

class BoardPopulation private constructor(
    val population: Collection<Board>,
    val fitnessFunction: Evaluator
) {
    constructor(
        populationGenerator: PopulationGenerator,
        fitnessFunction: Evaluator
    ) : this(populationGenerator.generatePopulation(), fitnessFunction)

    fun selection(selectionPopulationSize: Int) : Collection<Board> {
        return population.sortedBy { b -> fitnessFunction.evaluate(b) }.take(selectionPopulationSize)
    }
}