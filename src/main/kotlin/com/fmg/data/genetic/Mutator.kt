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
        b.queens.forEach { queen ->
            if (RANDOM.nextDouble() < probability) {
                b = b.withoutQueen(queen).withQueen(Queen(queen.row, RANDOM.nextInt(size)))
            }
        }
        return b
    }
}

class SwapRowMutator(val probability: Double) : Mutator {
    override fun mutate(board: Board): Board {
        var queens = board.queens.toMutableList()
        for (i in 0 until board.size) {
            val queen = queens[i]
            if (RANDOM.nextDouble() < probability) {
                val row = RANDOM.nextInt(board.size)
                val q = board.queens.single { q -> q.row == row }

                if (row != queen.row) {
                    queens = (queens - queen - q + Queen(queen.row, q.col) + Queen(q.row, queen.col)).toMutableList()
                }
            }
        }
        board.queens.forEach { q -> board.withQueen(q) }
        queens.forEach { q -> board.withQueen(q) }
        return board
    }
}
