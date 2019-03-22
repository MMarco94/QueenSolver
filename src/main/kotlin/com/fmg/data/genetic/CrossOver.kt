package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.data.Queen

interface CrossOver {
    fun crossOver(boardPopulation: Collection<Board>): Collection<Board>
}

object RowQueenCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        val population = boardPopulation.toList()
        val returnBoardCollection = mutableListOf<Board>()

        for ((index, board1) in population.withIndex()) {
            val board2 = population[RANDOM.nextInt(index, population.size)]

            val firstRowIndexCrossOver = RANDOM.nextInt(board1.size)
            val secondRowIndexCrossOver = RANDOM.nextInt(firstRowIndexCrossOver, board1.size)

            var newBoard1 = board1
            var newBoard2 = board2
            for (i in firstRowIndexCrossOver until secondRowIndexCrossOver + 1) {
                val q1 = newBoard1.queens.single { q -> q.row == i }
                val q2 = newBoard2.queens.single { q -> q.row == i }

                newBoard1 = newBoard1.withoutQueen(q1).withQueen(q2)
                newBoard2 = newBoard2.withoutQueen(q2).withQueen(q1)
            }

            returnBoardCollection.add(newBoard1)
            returnBoardCollection.add(newBoard2)
        }

        return returnBoardCollection
    }
}


object SwapRowsCrossOver : CrossOver {

    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        val boardSize = boardPopulation.first().size
        val population = boardPopulation.toList()
        val returnBoardCollection = mutableListOf<Board>()

        for ((index, board1) in population.withIndex()) {
            val board2 = population[RANDOM.nextInt(index, population.size)]

            val firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
            val secondRowIndexCrossOver = RANDOM.nextInt(firstRowIndexCrossOver, boardSize)

            var newBoard1 = board1
            var newBoard2 = board2
            for (i in firstRowIndexCrossOver until secondRowIndexCrossOver + 1) {
                val q1 = newBoard1.queens.single { q -> q.row == i }
                val q2 = newBoard2.queens.single { q -> q.col == q1.col }

                if (q1.row != q2.row) {
                    val q3 = newBoard1.queens.single { q -> q.row == q2.row }
                    val q4 = newBoard2.queens.single { q -> q.row == q1.row }

                    newBoard1 = newBoard1.with(
                        toAddQueens = arrayOf(Queen(q1.row, q3.col), q2),
                        toRemoveQueens = arrayOf(q1, q3)
                    )

                    newBoard2 = newBoard2.with(
                        toAddQueens = arrayOf(Queen(q2.row, q4.col), q1),
                        toRemoveQueens = arrayOf(q2, q4)
                    )
                }
            }

            returnBoardCollection.add(newBoard1)
            returnBoardCollection.add(newBoard2)
        }
        return returnBoardCollection
    }
}
