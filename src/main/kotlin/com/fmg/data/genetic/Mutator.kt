package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.data.NeighborsGenerator
import com.fmg.data.Queen

interface Mutator {
    fun mutate(board: Board): Board
}

class ProbabilisticMutator(
    val mutator: Mutator,
    val probability: Double
) : Mutator {
    override fun mutate(board: Board): Board {
        return if (RANDOM.nextDouble() < probability) {
            mutator.mutate(board)
        } else {
            board
        }
    }
}

class RandomNeighbourMutator(val neighborsGenerator: NeighborsGenerator) : Mutator {
    override fun mutate(board: Board): Board {
        return neighborsGenerator.generateNeighbors(board).toList().random(RANDOM)
    }
}

class BasicMutator(val size: Int, val probability: Double) : Mutator {
    override fun mutate(board: Board): Board {
        var b = board
        board.queens.forEach { queen ->
            if (RANDOM.nextDouble() < probability)
                b = board.withoutQueen(queen).withQueen(Queen(queen.row, RANDOM.nextInt(size)))
        }
        return b
    }
}
