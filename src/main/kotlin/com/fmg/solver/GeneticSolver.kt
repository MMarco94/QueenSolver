package com.fmg.solver

import com.fmg.data.Board
import com.fmg.data.TotalConflictEvaluator
import com.fmg.data.genetic.*
import com.fmg.takeWhileInclusive

class GeneticSolver(
    private val populationGenerator: PopulationGenerator,
    private val selector: Selector,
    private val crossOver: CrossOver,
    private val mutator: Mutator,
    val repalcer: Replacer
) : Solver() {
    fun createPopulationSequence(size: Int): Sequence<Collection<Board>> {
        return generateSequence(populationGenerator.generatePopulation(size)) { population ->
            val selected = selector.select(population)
            val crossOvered = crossOver.crossOver(selected)
            val mutated = crossOvered.map { c -> mutator.mutate(c) }
            repalcer.replace(population + mutated, population.size)
        }
    }

    override fun createApproximationSequence(size: Int): Sequence<Board> {
        return createPopulationSequence(size)
            .map { it.firstOrNull { b -> b.isNQueenSolution() } ?: it.first() }
            .takeWhileInclusive { !it.isNQueenSolution() }
    }
}
