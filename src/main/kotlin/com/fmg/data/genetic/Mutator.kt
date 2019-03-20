package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.data.Queen

interface Mutator {

    fun mutate(board: Board, probability: Double = 1 / (board.size.toDouble() * board.size.toDouble())): Board
}

object HorizontalQueenMoverMutator : Mutator {
    override fun mutate(board: Board, probability: Double): Board {
        var b = board
        b.queens.forEach { queen ->
            if (RANDOM.nextDouble() < probability) {
                b = b.withoutQueen(queen).withQueen(Queen(queen.row, RANDOM.nextInt(b.size)))
            }
        }
        return b
    }
}


object SwapRowMutator : Mutator {
    override fun mutate(board: Board, probability: Double): Board {
        var newBoard = board
        for (q1 in newBoard.queens) {
            if (RANDOM.nextDouble() < probability) {
                val row = RANDOM.nextInt(newBoard.size)
                val q2 = newBoard.queens.single { q -> q.row == row }

                if (row != q1.row) {
                    newBoard = newBoard.with(
                        toAddQueens = arrayOf(Queen(q1.row, q2.col), Queen(q2.row, q1.col)),
                        toRemoveQueens = arrayOf(q1, q2)
                    )
                }
            }
        }
        return newBoard
    }
}
