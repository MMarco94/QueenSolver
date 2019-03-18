package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.*

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
            var firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
            var secondRowIndexCrossOver = RANDOM.nextInt(boardSize)
            var firstBoard = RANDOM.nextInt(boardPopulation.size)
            var secondBoard = RANDOM.nextInt(boardPopulation.size)

            while (firstRowIndexCrossOver >= secondRowIndexCrossOver) {
                firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
                secondRowIndexCrossOver = RANDOM.nextInt(boardSize)
            }

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

/*
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
*/

/*
class IsThisACrossOver (private val evaluator: BoardEvaluator = TotalConflictEvaluator) : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>, populationSize: Int): Collection<Board> {
        if (populationSize % 2 != 0) {
            throw IllegalArgumentException("The cardinality of the selected population must be even")
        }

        val boardSize = boardPopulation.first().size
        val population = boardPopulation.toMutableList()
        val returnBoardCollection = mutableListOf<Board>()

        while (returnBoardCollection.size < populationSize) {
            var firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
            var secondRowIndexCrossOver = RANDOM.nextInt(boardSize)
            var firstBoard = RANDOM.nextInt(boardPopulation.size)
            var secondBoard = RANDOM.nextInt(boardPopulation.size)

            while (firstRowIndexCrossOver >= secondRowIndexCrossOver) {
                firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
                secondRowIndexCrossOver = RANDOM.nextInt(boardSize)
            }

            while (firstBoard == secondBoard) {
                firstBoard = RANDOM.nextInt(population.size)
                secondBoard = RANDOM.nextInt(population.size)
            }

            var board1 = population[firstBoard]
            var board2 = population[secondBoard]

            for (i in firstRowIndexCrossOver until secondRowIndexCrossOver) {
                val q1 = board1.queens.single { q -> q.row == i }
                val q2 = board2.queens.single { q -> q.col == q1.col }

                if (q1.row != q2.row) {
                    val q3 = board1.queens.single { q -> q.row == q2.row }
                    val q4 = board2.queens.single { q -> q.row == q1.row }

                    if(evaluator.evaluate(board1.withQueen(Queen(q1.row, q3.col)).withQueen(q2)
                        .withoutQueen(q1)
                        .withoutQueen(q3)) < evaluator.evaluate(board1)) {
                        board1 = board1.withQueen(Queen(q1.row, q3.col)).withQueen(q2)
                            .withoutQueen(q1)
                            .withoutQueen(q3)
                    }

                    if (evaluator.evaluate(board2.withQueen(Queen(q2.row, q4.col)).withQueen(q1)
                        .withoutQueen(q2)
                        .withoutQueen(q4)) < evaluator.evaluate(board2)) {
                        board2 = board2.withQueen(Queen(q2.row, q4.col)).withQueen(q1)
                            .withoutQueen(q2)
                            .withoutQueen(q4)
                    }
                }
            }

            returnBoardCollection.add(board1)
            returnBoardCollection.add(board2)
        }

        return returnBoardCollection
    }
}
*/

object SwapRowsCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        val boardSize = boardPopulation.first().size
        val population = boardPopulation.toMutableList()
        val returnBoardCollection = mutableListOf<Board>()

        while (population.isNotEmpty()) {
            var firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
            var secondRowIndexCrossOver = RANDOM.nextInt(boardSize)

            while (firstRowIndexCrossOver >= secondRowIndexCrossOver) {
                firstRowIndexCrossOver = RANDOM.nextInt(boardSize)
                secondRowIndexCrossOver = RANDOM.nextInt(boardSize)
            }

            var board1 = population.removeAt(0)
            var board2 = population.random()

            population.remove(board2)

            for (i in firstRowIndexCrossOver until secondRowIndexCrossOver) {
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
        return returnBoardCollection
    }
}
