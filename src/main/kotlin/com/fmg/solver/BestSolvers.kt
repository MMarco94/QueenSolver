package com.fmg.solver

import com.fmg.data.*
import com.fmg.data.genetic.FitnessSelector
import com.fmg.data.genetic.IndependentPopulationGenerator
import com.fmg.data.genetic.SwapRowMutator
import com.fmg.data.genetic.SwapRowsCrossOver

object BestSolvers {


    val BEST_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator
    )
    val BEST_HILL_CLIMBING_SOLVER_GO_ON = HillClimbingSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenSolved
    )
    val SINGLE_QUEEN_MOVER_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        HorizontalQueenMoverNeighborsGenerator,
        OneQueenPerRowRandomBoardGenerator
    )
    val BLUE_TORNADO_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        KQueensMoverNeighborsGenerator(2),
        OneQueenPerRowRandomBoardGenerator
    )
    val CONSTRAINT_PROPAGATION_SOLVER = ConstraintPropagationAndBacktrackingSolver()
    val BEST_GENETIC_SOLVER = GeneticSolver(
        IndependentPopulationGenerator(OneQueenPerRowAndColumnRandomBoardGenerator, 1000),
        FitnessSelector(100, TotalConflictEvaluator),
        SwapRowsCrossOver,
        SwapRowMutator(0.01)
    )

    private val KRONECKER_BOARD_GENERATOR = FactorizerBoardGenerator(BEST_HILL_CLIMBING_SOLVER)

    private val KRONECKER_APPROXIMATE_BOARD_GENERATOR = FactorizerBoardApproximateGenerator(BEST_HILL_CLIMBING_SOLVER)

    val KRONECKER_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        KRONECKER_BOARD_GENERATOR
    )

    val APPROXIMATE_KRONECKER_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        KRONECKER_APPROXIMATE_BOARD_GENERATOR
    )

    val ALL_SOLVERS = mapOf(
        "Hill Climbing with Approximate Kr0nEcKeR" to APPROXIMATE_KRONECKER_HILL_CLIMBING_SOLVER,
        "Hill Climbing with Kr0nEcKeR" to KRONECKER_HILL_CLIMBING_SOLVER,
        "Hill Climbing log row swapper" to BEST_HILL_CLIMBING_SOLVER,
        "Hill Climbing log row swapper go on" to BEST_HILL_CLIMBING_SOLVER_GO_ON,
        "Hill Climbing" to SINGLE_QUEEN_MOVER_HILL_CLIMBING_SOLVER,
        "Hill Climbing with multiple moves" to BLUE_TORNADO_HILL_CLIMBING_SOLVER,
        "Constraint propagation" to CONSTRAINT_PROPAGATION_SOLVER,
        "Genetic Algorithm with Queens on Different Rows and Columns" to BEST_GENETIC_SOLVER
    )
}