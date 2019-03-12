package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.data.Queen

interface CrossOver {
    fun crossOver(boardPopulation: Collection<Board>): Collection<Board>
}

class TrivialCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        return boardPopulation
    }
}

object RowQueenCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        if (boardPopulation.size % 2 != 0) {
            throw IllegalArgumentException("The cardinality of the selected population must be even")
        }

        val boardSize = boardPopulation.first().size
        val rowIndexCrossOver = RANDOM.nextInt(boardSize)
        val population = boardPopulation.toMutableList()
        val returnBoardCollection = mutableListOf<Board>()

        while (population.isNotEmpty()) {
            var board1 = population.removeAt(0)
            var board2 = population.removeAt(0)

            for (i in rowIndexCrossOver until boardSize) {
                var q1 = board1.queens.single { q -> q.row == i }
                var q2 = board2.queens.single { q -> q.row == i }

                board1 = board1.withoutQueen(q1).withQueen(q2)
                board2 = board2.withoutQueen(q2).withQueen(q1)
            }

            returnBoardCollection.add(board1)
            returnBoardCollection.add(board2)
        }

        return returnBoardCollection
    }
}

object RowQueenWithColumnCheckCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        if (boardPopulation.size % 2 != 0) {
            throw IllegalArgumentException("The cardinality of the selected population must be even")
        }

        val boardSize = boardPopulation.first().size
        val rowIndexCrossOver = RANDOM.nextInt(boardSize)
        val population = boardPopulation.toMutableList()
        val returnBoardCollection = mutableListOf<Board>()

        while (population.isNotEmpty()) {
            var board1 = population.removeAt(0)
            var board2 = population.removeAt(0)

            for (i in rowIndexCrossOver until boardSize) {
                var q1 = board1.queens.single { q -> q.row == i }
                var q2 = board2.queens.single { q -> q.row == i }

                if (board1.queens.filter { q -> q.col == q2.col }.isEmpty()) {
                    board1 = board1.withoutQueen(q1).withQueen(q2)
                }
                if (board2.queens.filter { q -> q.col == q1.col }.isEmpty()) {
                    board2 = board2.withoutQueen(q2).withQueen(q1)
                }
            }

            returnBoardCollection.add(board1)
            returnBoardCollection.add(board2)
        }

        return returnBoardCollection.reversed()
    }
}

object SwapRowsCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        if (boardPopulation.size % 2 != 0) {
            throw IllegalArgumentException("The cardinality of the selected population must be even")
        }

        val boardSize = boardPopulation.first().size
        val rowIndexCrossOver = RANDOM.nextInt(boardSize)
        val population = boardPopulation.toMutableList()
        val returnBoardCollection = mutableListOf<Board>()

        while (population.isNotEmpty()) {
            var board1 = population.removeAt(0)
            var board2 = population.removeAt(0)

            for (i in rowIndexCrossOver until boardSize) {
                val q1 = board1.queens.single { q -> q.row == i }
                val q2 = board2.queens.single { q -> q.col == q1.col }

                if (q1.row != q2.row) {
                    val q3 = board1.queens.single { q -> q.row == q2.row }
                    val q4 = board2.queens.single { q -> q.row == q1.row }

                    board1 = board1.withQueen(Queen(q1.row, q3.col)).withQueen(q2)
                        .withoutQueen(q1)
                        .withoutQueen(q3)

                    board2 = board2.withQueen(Queen(q2.row, q4.col)).withQueen(q1)
                        .withoutQueen(q2)
                        .withoutQueen(q4)
                }
            }

            returnBoardCollection.add(board1)
            returnBoardCollection.add(board2)
        }

        return returnBoardCollection.reversed()
    }
}


