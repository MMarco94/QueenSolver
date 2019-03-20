package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.data.OneQueenPerRowAndColumnRandomBoardGenerator
import com.fmg.data.Queen

interface CrossOver {
    fun crossOver(boardPopulation: Collection<Board>): Collection<Board>
}

object CompletelyRandomCrossover : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        val size = boardPopulation.first().size
        return generateSequence {
            OneQueenPerRowAndColumnRandomBoardGenerator.generateBoard(size)
        }.take(boardPopulation.size).toList()
    }
}

object RowQueenCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        val boardSize = boardPopulation.first().size
        val population = boardPopulation.toMutableList()
        val returnBoardCollection = mutableListOf<Board>()

        while (population.isNotEmpty()) {
            val firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
            val secondRowIndexCrossOver = RANDOM.nextInt(firstRowIndexCrossOver, boardSize)
            var firstBoard = RANDOM.nextInt(boardPopulation.size)
            var secondBoard = RANDOM.nextInt(boardPopulation.size)

            while (firstBoard == secondBoard) {
                firstBoard = RANDOM.nextInt(population.size)
                secondBoard = RANDOM.nextInt(population.size)
            }

            var board1 = population.removeAt(firstBoard)
            var board2 = population.removeAt(secondBoard)

            for (i in firstRowIndexCrossOver until secondRowIndexCrossOver) {
                val q1 = board1.queens.single { q -> q.row == i }
                val q2 = board2.queens.single { q -> q.row == i }

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
                val q1 = board1.queens.single { q -> q.row == i }
                val q2 = board2.queens.single { q -> q.row == i }

                if (board1.queens.none { q -> q.col == q2.col }) {
                    board1 = board1.withoutQueen(q1).withQueen(q2)
                }
                if (board2.queens.none { q -> q.col == q1.col }) {
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
        val boardSize = boardPopulation.first().size
        val population = boardPopulation.toList()
        val returnBoardCollection = mutableListOf<Board>()

        for ((index, board1) in population.withIndex()) {
            val board2 = population[RANDOM.nextInt(index, population.size)]

            val firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
            val secondRowIndexCrossOver = RANDOM.nextInt(firstRowIndexCrossOver, boardSize)

            var newBoard1 = board1
            var newBoard2 = board2
            for (i in firstRowIndexCrossOver until secondRowIndexCrossOver) {
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
