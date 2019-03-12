package com.fmg.solver

import com.fmg.Factorizer
import com.fmg.TrivialFactorizer
import com.fmg.data.Board

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
                s1.zip(s2) { b1, b2 ->
                    b1 * b2
                }
            }
    }
}