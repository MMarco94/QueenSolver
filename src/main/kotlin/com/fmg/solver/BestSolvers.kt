package com.fmg.solver

import com.fmg.data.*
import com.fmg.data.genetic.*

object BestSolvers {


    val BEST_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictBoardEvaluator,
        LogRowSwapperNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenNotImproving//TODO
    )

    val NEW_SOLVER = HillClimbingSolver(
        TotalConflictBoardEvaluator,
        RowSwapperNeighborsGenerator(),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenNotImproving//TODO
    )

    val SINGLE_MOVE = HillClimbingSolver(
        TotalConflictBoardEvaluator,
        HorizontalQueenMoverNeighborsGenerator,
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenNotImproving
    )

    val BEST_DETERMINISTIC_HILL_CLIMBING  = HillClimbingSolver(
        TotalConflictBoardEvaluator,
        RowSwapperWithQueenHeuristicNeighborsGenerator (),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        TerminateWhenNotImproving
    )

    val SIMULATED_ANNELING = SimulatedAnnealingSolver(
        TotalConflictBoardEvaluator,
        RowSwapperWithQueenHeuristicNeighborsGenerator (),
        OneQueenPerRowAndColumnRandomBoardGenerator,
        UniformDistributionProbabilisticSelector
    )
    val SINGLE_QUEEN_MOVER_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictBoardEvaluator,
        HorizontalQueenMoverNeighborsGenerator,
        OneQueenPerRowRandomBoardGenerator,
        TerminateWhenNoConflicts//TODO
    )
    val BLUE_TORNADO_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictBoardEvaluator,
        KQueensMoverNeighborsGenerator(2),
        OneQueenPerRowRandomBoardGenerator,
        TerminateWhenNoConflicts//TODO
    )
    val CONSTRAINT_PROPAGATION_SOLVER = ConstraintPropagationAndBacktrackingSolver()

    val BEST_GENETIC_SOLVER_SWAP = GeneticSolver(
        IndependentPopulationGenerator(),
        BestSelector(),
        SwapRowsCrossOver,
        SwapRowMutator,
        LambdaMuReplacer()
    )

    val BEST_GENETIC_SOLVER_ROW = GeneticSolver(
        IndependentPopulationGenerator(OneQueenPerRowRandomBoardGenerator),
        FitnessProportionalSelector(),
        RowQueenCrossOver,
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

    val FITNESS_PROPORTIONAL_GENETIC_SOLVER = GeneticSolver(
        IndependentPopulationGenerator(),
        FitnessProportionalSelector(),
        SwapRowsCrossOver,
        SwapRowMutator,
        LambdaMuReplacer()
    )

    private val KRONECKER_BOARD_GENERATOR = FactorizerBoardGenerator(BEST_HILL_CLIMBING_SOLVER)

    val KRONECKER_HILL_CLIMBING_SOLVER = HillClimbingSolver(
        TotalConflictBoardEvaluator,
        LogRowSwapperNeighborsGenerator(),
        KRONECKER_BOARD_GENERATOR,
        TerminateWhenNoConflicts//TODO
    )

    val KRONECKER_ROULETTE_WHEEL_GENETIC_SOLVER = GeneticSolver(
        IndependentPopulationGenerator(KRONECKER_BOARD_GENERATOR),
        RouletteWheelSelector(),
        SwapRowsCrossOver,
        SwapRowMutator,
        LambdaMuReplacer()
    )

    val ONE_ROW_QUEEN_ROULETTE_WHEEL_GENETIC_SOLVER = GeneticSolver(
        IndependentPopulationGenerator(OneQueenPerRowRandomBoardGenerator),
        RouletteWheelSelector(),
        RowQueenCrossOver,
        HorizontalQueenMoverMutator,
        LambdaMuReplacer()
    )

    val ALL_SOLVERS = mapOf(
        /*"Genetic Algorithm with Queens on Different Rows and Columns with roulette wheel selection" to ROULETTE_WHEEL_GENETIC_SOLVER,
        "Genetic Algorithm with Queens on Different Rows and Columns with fitness proportional selection" to FITNESS_PROPORTIONAL_GENETIC_SOLVER,
        "Genetic Algorithm with Queens on Different Rows and Columns with non darwinian selection" to BEST_GENETIC_SOLVER_SWAP,
        "Genetic Algorithm with Queens on Different Rows with fitness proportional selection" to BEST_GENETIC_SOLVER_ROW

        "Simulated annealing" to SIMULATED_ANNELING,
        "Constraint propagation" to CONSTRAINT_PROPAGATION_SOLVER*/


        /*"Hill Climbing with Kℝ0ℕ€cK€ℝ" to KRONECKER_HILL_CLIMBING_SOLVER,
        "Hill Climbing log row swapper" to BEST_HILL_CLIMBING_SOLVER,
        */


        /*"New one" to NEW_SOLVER,
        "Single move"  to SINGLE_MOVE*/


        "Deterministic hill climbing" to BEST_DETERMINISTIC_HILL_CLIMBING,
        "Simulated annealing" to SIMULATED_ANNELING


        /*
        "Simulated annealing" to SIMULATED_ANNELING,
        "Constraint propagation" to CONSTRAINT_PROPAGATION_SOLVER,
        "Hill Climbing" to SINGLE_QUEEN_MOVER_HILL_CLIMBING_SOLVER,

        "Kronecker Genetic Algorithm" to KRONECKER_ROULETTE_WHEEL_GENETIC_SOLVER*/
    )
}