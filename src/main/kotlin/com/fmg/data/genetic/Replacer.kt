package com.fmg.data.genetic

import com.fmg.data.Board

interface Replacer {
    fun replace(bigPopulation : Collection<Board>, populationSize : Int) : Collection<Board>
}

class LambdaMuReplacer(private val selector: Selector = FitnessSelector()) : Replacer {
    override fun replace(bigPopulation: Collection<Board>, populationSize: Int): Collection<Board> {
        return selector.select(bigPopulation, populationSize)
    }
}