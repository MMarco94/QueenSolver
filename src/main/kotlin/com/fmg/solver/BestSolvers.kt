package com.fmg.solver

import com.fmg.data.*
import com.fmg.data.genetic.*

object BestSolvers {


    val BEST_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenNotImproving//TODO
    )

    val TABU_SEARCH_SOLVER = TabuSerachSolver(
        TotalConflictEvaluator,
        LogRowSwapperNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenSolved//TODO
    )

    val BEST_DETERMINISTIC_HILL_CLIMBING  = HillClimbingSolver(
        TotalConflictEvaluator,
        DeterministicOneQueenPerRowAndColumnNeighborsGenerator (),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenNotImproving
    )

    val SIMULATED_ANNELING = SimulatedAnnealingSolver(
        TotalConflictEvaluator,
        SimulatedAnnealingNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator
    )
    val SINGLE_QUEEN_MOVER_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        HorizontalQueenMoverNeighborsGenerator,
        OneQueenPerRowRandomBoardGenerator,
        TerminateWhenSolved//TODO
    )
    val BLUE_TORNADO_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictEvaluator,
        KQueensMoverNeighborsGenerator(2),
        OneQueenPerRowRandomBoardGenerator,
        TerminateWhenSolved//TODO
    )
    val CONSTRAINT_PROPAGATION_SOLVER = ConstraintPropagationAndBacktrackingSolver()

    val BEST_GENETIC_SOLVER = GeneticSolver(
        IndependentPopulationGenerator(),
        FitnessSelector(),
        SwapRowsCrossOver,
        SwapRowMutator,
        LambdaMuReplacer()
    )

    val ROULETTE_WHEEL_GENETIC_SOLVER = GeneticSolver(
        IndependentPopulationGenerator(),
        RouletteWheelSelector(),
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

    val KRONECKER_ROULETTE_WHEEL_GENETIC_SOLVER = GeneticSolver(
        IndependentPopulationGenerator(KRONECKER_BOARD_GENERATOR),
        RouletteWheelSelector(),
        SwapRowsCrossOver,
        SwapRowMutator,
        LambdaMuReplacer()
    )

    val ALL_SOLVERS = mapOf(
        "Hill Climbing with Kℝ0ℕ€cK€ℝ" to KRONECKER_HILL_CLIMBING_SOLVER,
        "Hill Climbing log row swapper" to BEST_HILL_CLIMBING_SOLVER,
        "Deterministic hill climbing" to BEST_DETERMINISTIC_HILL_CLIMBING,
        "Simulated annealing" to SIMULATED_ANNELING,
        "Constraint propagation" to CONSTRAINT_PROPAGATION_SOLVER,
        "Hill Climbing" to SINGLE_QUEEN_MOVER_HILL_CLIMBING_SOLVER,
        "Hill Climbing with multiple moves" to BLUE_TORNADO_HILL_CLIMBING_SOLVER,
        //"Genetic Algorithm with Queens on Different Rows and Columns with static selection" to BEST_GENETIC_SOLVER,
        //"Genetic Algorithm with Queens on Different Rows and Columns with roulette wheel selection" to ROULETTE_WHEEL_GENETIC_SOLVER,
        "Kronecker Genetic Algorithm" to KRONECKER_ROULETTE_WHEEL_GENETIC_SOLVER
    )
}