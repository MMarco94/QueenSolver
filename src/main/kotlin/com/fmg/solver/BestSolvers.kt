package com.fmg.solver

import com.fmg.data.*
import com.fmg.data.genetic.*

object BestSolvers {


    val BEST_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenSolved//TODO
    )
    val SIMULATED_ANNELING = SimulatedAnnealingSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator
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
        IndependentPopulationGenerator(),
        FitnessSelector(),
        SwapRowsCrossOver,
        SwapRowMutator,
        LambdaMuReplacer()
    )

    private val KRONECKER_BOARD_GENERATOR = FactorizerBoardGenerator(BEST_HILL_CLIMBING_SOLVER)

    val KRONECKER_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        KRONECKER_BOARD_GENERATOR,
        TerminateWhenSolved//TODO
    )

    val ALL_SOLVERS = mapOf(
        "Hill Climbing log row swapper" to BEST_HILL_CLIMBING_SOLVER,
        "Hill Climbing with Kr0nEcKeR" to KRONECKER_HILL_CLIMBING_SOLVER,
        "Simulated annealing" to SIMULATED_ANNELING,
        "Hill Climbing" to SINGLE_QUEEN_MOVER_HILL_CLIMBING_SOLVER,
        "Hill Climbing with multiple moves" to BLUE_TORNADO_HILL_CLIMBING_SOLVER,
        "Constraint propagation" to CONSTRAINT_PROPAGATION_SOLVER,
        "Genetic Algorithm with Queens on Different Rows and Columns" to BEST_GENETIC_SOLVER
    )
}