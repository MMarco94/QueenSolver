package com.fmg.data.genetic

import com.fmg.RANDOM
import com.fmg.data.Board
import com.fmg.solver.UnableToSolveException
import java.lang.Exception

interface CrossOver {
    fun crossOver(boardPopulation: Collection<Board>): Collection<Board>
}


class TrivialCrossOver : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        return boardPopulation
    }
}

class RowQueenCrossOver(val boardSize: Int) : CrossOver {
    override fun crossOver(boardPopulation: Collection<Board>): Collection<Board> {
        if (boardPopulation.size % 2 != 0) {
            throw Exception("The cardinality of the selected population must be even")
        }

        val rowIndexCrossOver = RANDOM.nextInt(boardSize)
        var population = boardPopulation.toMutableList()
        var returnBoardCollection = mutableListOf<Board>()

        while (population.size > 0) {
            var board1 = population.removeAt(0)
            var board2 = population.removeAt(0)

            for (i in rowIndexCrossOver until boardSize) {
                var q1 = board1.queens.filter { q -> q.row == i }.first()
                var q2 = board2.queens.filter { q -> q.row == i }.first()

                board1 = board1.withoutQueen(q1).withQueen(q2)
                board2 = board2.withoutQueen(q2).withQueen(q1)

                returnBoardCollection.add(board1)
                returnBoardCollection.add(board2)
            }
        }

        return returnBoardCollection
    }
}


