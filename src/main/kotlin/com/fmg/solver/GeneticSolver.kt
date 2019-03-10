package com.fmg.solver

import com.fmg.data.Board
import com.fmg.data.genetic.CrossOver
import com.fmg.data.genetic.Mutator
import com.fmg.data.genetic.PopulationGenerator
import com.fmg.data.genetic.Selector
import com.fmg.takeWhileInclusive

class GeneticSolver(
    size: Int,
    val populationGenerator: PopulationGenerator,
    val selector: Selector,
    val crossOver: CrossOver,
    val mutator: Mutator
) : GlobalOptimizationSolver(size) {
    fun createPopulationSequence(): Sequence<Collection<Board>> {
        return generateSequence(populationGenerator.generatePopulation()) { population ->
            val selected = selector.select(population)
            val crossOvered = crossOver.crossOver(selected)
            val mutated = crossOvered.map { c -> mutator.mutate(c) }
            population - selected + mutated
        }
    }

    override fun createApproximationSequence(): Sequence<Board> {
        return createPopulationSequence()
            .map { it.firstOrNull { b -> b.isNQueenSolution() } ?: it.last() }
            .takeWhileInclusive { !it.isNQueenSolution() }
    }
}