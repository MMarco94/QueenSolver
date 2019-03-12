package com.fmg.solver

import com.fmg.Factorizer
import com.fmg.TrivialFactorizer
import com.fmg.data.Board
import com.fmg.repeatLastElement
import com.fmg.takeWhileInclusive

class FactorizerSolver(
    val solver: Solver,
    val factorizer: Factorizer = TrivialFactorizer
) : Solver() {

    override fun createApproximationSequence(size: Int): Sequence<Board> {
        return factorizer.factorize(size, 4)
            .map { f ->
                solver.createApproximationSequence(f)
            }
            .reduce { s1, s2 ->
                s1.repeatLastElement().zip(s2.repeatLastElement()) { b1, b2 ->
                    b1 * b2
                }
            }
            .takeWhileInclusive { b -> !b.isNQueenSolution() }
    }
}