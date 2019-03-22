package com.fmg.data.genetic

import com.fmg.data.Board

interface Replacer {
    fun replace(
        oldPopulation: Collection<Board>,
        newPopulation: Collection<Board>,
        populationSize: Int
    ): Collection<Board>
}

class LambdaMuReplacer(private val selector: Selector = BestSelector()) : Replacer {

    override fun replace(
        oldPopulation: Collection<Board>,
        newPopulation: Collection<Board>,
        populationSize: Int
    ): Collection<Board> {
        return selector.select(oldPopulation + newPopulation, populationSize)
    }
}