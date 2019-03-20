package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.data.Queen

interface Mutator {

    fun mutate(board: Board, probability: Double = 1 / (board.size.toDouble() * board.size.toDouble())): Board
}

class BasicMutator(val size: Int) : Mutator {
    override fun mutate(board: Board, probability: Double): Board {
        var b = board
        b.queens.forEach { queen ->
            if (RANDOM.nextDouble() < probability) {
                b = b.withoutQueen(queen).withQueen(Queen(queen.row, RANDOM.nextInt(size)))
            }
        }
        return b
    }
}


object SwapRowMutator : Mutator {
    override fun mutate(board: Board, probability: Double): Board {
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
