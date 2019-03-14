package com.fmg.solver

import com.fmg.data.Board
import com.fmg.data.TotalConflictEvaluator
import com.fmg.data.genetic.CrossOver
import com.fmg.data.genetic.Mutator
import com.fmg.data.genetic.PopulationGenerator
import com.fmg.data.genetic.Selector
import com.fmg.takeWhileInclusive

class GeneticSolver(
    val populationGenerator: PopulationGenerator,
    val selector: Selector,
    val crossOver: CrossOver,
    val mutator: Mutator
) : Solver() {
    fun createPopulationSequence(size: Int): Sequence<Collection<Board>> {
        return generateSequence(populationGenerator.generatePopulation(size)) { population ->
            val selected = selector.select(population)
            val crossOvered = crossOver.crossOver(selected)
            val mutated = crossOvered.map { c -> mutator.mutate(c) }
            /*val np = selector.select((population + mutated), population.size)

            var sum : Double = 0.0
            for (i in np) {
                sum += TotalConflictEvaluator.evaluate(i)
            }
            println(sum / np.size)*/

            selector.select((population + mutated), population.size)
        }
    }

    override fun createApproximationSequence(size: Int): Sequence<Board> {
        return createPopulationSequence(size)
            .map { it.firstOrNull { b -> b.isNQueenSolution() } ?: it.first() }
            .takeWhileInclusive { !it.isNQueenSolution() }
    }
}
