package com.fmg.data.genetic

import com.fmg.data.Board

interface CrossOver {
    fun crossOver (boardPopulation: Collection<Board>) : Collection<Board>
}


class TrivialCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        return boardPopulation
    }

}

//TODO: implement a real crossover

