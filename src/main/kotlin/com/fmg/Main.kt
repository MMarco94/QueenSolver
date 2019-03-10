package com.fmg

import com.fmg.data.*
import com.fmg.data.genetic.*
import com.fmg.solver.ConstraintPropagationAndBacktrackingSolver
import com.fmg.solver.GeneticSolver
import com.fmg.solver.HillClimbingSolver
import com.fmg.solver.Solver
import java.util.*
import kotlin.random.Random

val RANDOM = Random(42)

val scanner = Scanner(System.`in`)

fun getAllSolvers(size: Int) = mapOf(
    "Constraint propagation" to ConstraintPropagationAndBacktrackingSolver(size),
    "Hill Climbing" to HillClimbingSolver(
        size,
        TotalConflictEvaluator,
        HorizontalQueenMoverNeighborsGenerator,
        OneQueenPerRowRandomBoardGenerator
    ),
    "Hill Climbing with free movement" to HillClimbingSolver(
        size,
        TotalConflictEvaluator,
        TwoQueenMoverNeighborsGenerator,
        RandomBoardGenerator
    ),
    "Genetic Algorithm Queens on Different Rows" to GeneticSolver(
        size,
        IndependentPopulationGenerator(size, OneQueenPerRowRandomBoardGenerator, 1000),
        FitnessSelector(500, ConflictEvaluator),
        RowQueenCrossOver(size),
        BasicMutator(size, 0.1)
    )
)

fun readInt(message: String): Int {
    println(message)
    while (true) {
        val nextLine = scanner.nextLine()
        try {
            return Integer.parseInt(nextLine)
        } catch (e: NumberFormatException) {
            println("Invalid number!")
        }
    }
}

fun readChoice(options: Collection<String>): Int {
    println("Choose one of the following:")
    for ((i, option) in options.withIndex()) {
        println("${i + 1}) $option")
    }
    while (true) {
        val readInt = readInt("Enter a number between 1 and ${options.size}")
        if (readInt > 0 && readInt <= options.size) {
            return readInt - 1
        }
    }
}

fun chooseSolver(size: Int): Solver {
    val allSolvers = getAllSolvers(size)
    return allSolvers.values.elementAt(readChoice(allSolvers.keys))
}

fun main() {
    val size = readInt("Enter the board size")
    when (readChoice(listOf("Step by step", "Solve", "Benchmark"))) {
        0 -> {//Step by step
            printSteps(chooseSolver(size).createApproximationSequence())
        }
        1 -> {//Solution
            printSolution(
                chooseSolver(size)
                    .createApproximationSequence()
                    .take(readInt("Choose a maximum number of steps"))
            )
        }
        2 -> {//Benchmark
            val trials = readInt("Enter the number of trials")
            val maxSteps = readInt("Choose a maximum number of steps")



            for ((name, solver) in getAllSolvers(size)) {
                val correctnessPercentageStat = DoubleSummaryStatistics()
                val timeStat = DoubleSummaryStatistics()
                val stepsStat = LongSummaryStatistics()

                print("Computing using solver $name")
                for (i in 0 until trials) {
                    print(".")
                    val approximationSequence = solver.createApproximationSequence()

                    val (took, ss) = benchmark {
                        approximationSequence
                            .take(maxSteps)
                            .withIndex()
                            .last()
                    }
                    val (steps, solution) = ss

                    timeStat.accept(took)
                    stepsStat.accept(steps + 1)
                    correctnessPercentageStat.accept(if (solution.isNQueenSolution()) 1.0 else 0.0)
                }
                println()
                println("Time statistics of $name: $timeStat")
                println("Steps statistics of $name: $stepsStat")
                println("Correctness probability of $name: ${correctnessPercentageStat.average * 100}%")
                println()
                println()
            }
            enterToExit()
        }
        else -> throw IllegalStateException()
    }

}

private fun printSteps(steps: Sequence<Board>) {
    steps.forEachIndexed { step, partialSolution ->
        val totalConflicts = TotalConflictEvaluator.evaluate(partialSolution)
        val conflicts = ConflictEvaluator.evaluate(partialSolution)
        println("Step $step (${partialSolution.queens.size} queens, $totalConflicts totalConflicts, $conflicts conflicts):")
        partialSolution.print()
        println()
        println("Press enter to continue")
        scanner.nextLine()
    }
    println("Finished!!!")
    enterToExit()
}

private fun printSolution(approximationSequence: Sequence<Board>) {
    val (took, result) = benchmark {
        approximationSequence.withIndex().last()
    }
    val (steps, solution) = result

    if (solution.queens.size == solution.size && solution.isNQueenSolution()) {
        println("Solution in $steps steps:")
    } else {
        println("Best approximation after $steps steps:")
    }
    solution.print()
    println("Took ${took * 1000} ms")
    enterToExit()
}

private fun enterToExit() {
    println("Enter to exit")
    scanner.nextLine()
}

private fun <R> benchmark(subject: () -> R): Pair<Double, R> {
    val start = System.nanoTime()
    val ret = subject()
    return (System.nanoTime() - start) / 1000000000.0 to ret
}