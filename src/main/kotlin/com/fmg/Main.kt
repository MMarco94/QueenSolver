package com.fmg

import com.fmg.data.Board
import com.fmg.solver.ConstraintPropagationAndBacktrackingSolver
import com.fmg.solver.HillClimbingSolver
import com.fmg.solver.Solver
import java.util.*

val scanner = Scanner(System.`in`)

fun getAllSolvers(size: Int) = mapOf(
    "Constraint propagation" to ConstraintPropagationAndBacktrackingSolver(size),
    "Hill Climbing" to HillClimbingSolver(size, ConflictEvaluator)
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
            printSolution(chooseSolver(size).createApproximationSequence())
        }
        2 -> {//Benchmark
            val trials = readInt("Enter the number of trials")

            val timeStat = DoubleSummaryStatistics()
            val stepsStat = LongSummaryStatistics()


            for ((name, solver) in getAllSolvers(size)) {
                println("Computing solver $name")
                for (i in 0 until trials) {
                    val approximationSequence = solver.createApproximationSequence()

                    val (took, numberOfSteps) = benchmark {
                        approximationSequence.count()
                    }

                    timeStat.accept(took)
                    stepsStat.accept(numberOfSteps)
                }
                println("Time statistics of $name: $timeStat")
                println("Steps statistics of $name: $stepsStat \n\n")
            }
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
}

private fun printSolution(approximationSequence: Sequence<Board>) {
    val (took, result) = benchmark {
        approximationSequence.withIndex().last()
    }
    val (steps, solution) = result

    if (solution.queens.size == solution.size && solution.isValid()) {
        println("Solution in $steps steps:")
    } else {
        println("Best approximation after $steps steps:")
    }
    solution.print()
    println("Took ${took * 1000} ms")
}

private fun <R> benchmark(subject: () -> R): Pair<Double, R> {
    val start = System.nanoTime()
    val ret = subject()
    return (System.nanoTime() - start) / 1000000000.0 to ret
}