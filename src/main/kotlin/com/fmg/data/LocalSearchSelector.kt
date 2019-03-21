package com.fmg.data

import com.fmg.RANDOM
import com.fmg.allMinBy
import com.fmg.randomOrNull
import kotlin.math.pow

interface LocalSearchSelector {
    fun select(boardSequence: Sequence<BoardWithScore>): BoardWithScore
}

class GeometricDistributionProbabilisticSelector(
    val p: Double
) : LocalSearchSelector {
    override fun select(boardSequence: Sequence<BoardWithScore>): BoardWithScore {

        val randomNumber = RANDOM.nextDouble()

        return boardSequence.allMinBy { board -> board.score }
            .mapIndexed { index, boardWithScore ->
                Pair(boardWithScore, geometricDistributionCDF(index, p))
            }
            .first { (_, probValue) ->
                val x = probValue
                randomNumber < probValue
            }
            .let { (b, _) -> b }
    }

    private fun geometricDistributionCDF(k: Int, p: Double): Double {
        return 1 - ((1 - p).pow(k + 1));
    }
}

object UniformDistributionProbabilisticSelector : LocalSearchSelector {
    override fun select(boardSequence: Sequence<BoardWithScore>): BoardWithScore {
        return boardSequence
            .toList()
            .random()
    }
}

object MinimumProbabilisticSelector : LocalSearchSelector {
    override fun select(boardSequence: Sequence<BoardWithScore>): BoardWithScore {
        return boardSequence
            .allMinBy { board -> board.score }
            .random()
    }
}



