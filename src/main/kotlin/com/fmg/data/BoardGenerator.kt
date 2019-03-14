package com.fmg.data

import com.fmg.Factorizer
import com.fmg.RANDOM
import com.fmg.TrivialFactorizer
import com.fmg.repeatLastElement
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

class FactorizerBoardApproximateGenerator(
    val solver: Solver,
    val factorizer: Factorizer = TrivialFactorizer,
    val k: Int = 50,
    val fallbackBoardGenerator: BoardGenerator = OneQueenPerRowAndColumnRandomBoardGenerator
) : BoardGenerator {

    override fun generateBoard(size: Int): Board {
        return factorizer.factorize(size, 4)
            .map { f ->
                solver.createApproximationSequence(f).repeatLastElement(fallbackIfEmpty = {
                    fallbackBoardGenerator.generateBoard(size)
                })
            }
            .reduce { s1, s2 ->
                s1.zip(s2) { b1, b2 ->
                    b1 * b2
                }
            }
            .elementAt(k)
    }
}

class FactorizerBoardGenerator(
    val solver: Solver,
    val factorizer: Factorizer = TrivialFactorizer
) : BoardGenerator {

    override fun generateBoard(size: Int): Board {
        return factorizer.factorize(size, 4)
            .map { f ->
                solver.createApproximationSequence(f).last()
            }
            .reduce { b1, b2 ->
                b1 * b2
            }
    }
}