package com.fmg.data

import com.fmg.*
import com.fmg.solver.Solver

interface BoardGenerator {
    fun generateBoard(size: Int): Board
}

object RandomBoardGenerator : BoardGenerator {
    override fun generateBoard(size: Int): Board {
        val rnd = RANDOM
        return generateSequence(Board(size)) { prev ->
            if (prev.queens.size == size) {
                null
            } else {
                prev.withQueen(Queen(rnd.nextInt(size), rnd.nextInt(size)))
            }
        }.last()
    }
}

object OneQueenPerRowRandomBoardGenerator : BoardGenerator {
    override fun generateBoard(size: Int): Board {
        return generateSequence(Board(size)) { prev ->
            if (prev.queens.size == size) {
                null
            } else {
                prev.withQueen(Queen(prev.queens.size, RANDOM.nextInt(size)))
            }
        }.last()
    }
}

object OneQueenPerRowAndColumnRandomBoardGenerator : BoardGenerator {
    override fun generateBoard(size: Int): Board {
        val colPositions = mutableListOf<Int>()
        return generateSequence(Board(size)) { prev ->
            if (prev.queens.size == size) {
                null
            } else {
                var col = RANDOM.nextInt(size)
                while (colPositions.contains(col)) {
                    col = RANDOM.nextInt(size)
                }
                colPositions.add(col)
                prev.withQueen(Queen(prev.queens.size, col))
            }
        }.last()
    }
}

class FactorizerBoardGenerator(
    val solver: Solver,
    val factorizer: Factorizer = TrivialFactorizer,
    val limit: Int = 1000
) : BoardGenerator {

    override fun generateBoard(size: Int): Board {
        return factorizer.factorize(size, 4)
            .map { f ->
                solver
                    .createApproximationSequence(f)
                    .take(limit)
                    .last()
            }
            .reduce { b1, b2 ->
                b1 * b2
            }
    }
}

class BestOfKBoardGenerator(
    val generator: BoardGenerator,
    val k: Int,
    val evaluator: BoardEvaluator = TotalConflictEvaluator
) : BoardGenerator {
    override fun generateBoard(size: Int): Board {
        return generateSequence { generator.generateBoard(size).withScore(evaluator) }
            .take(k)
            .takeWhileInclusive { it.score > 0 }//If it is 0, I can terminate immediately
            .allMinBy { it.score }
            .random(RANDOM)
            .board
    }

}