package com.fmg

import com.fmg.data.Board
import com.fmg.data.ConflictEvaluator
import com.fmg.data.FactorizerBoardGenerator
import com.fmg.data.TotalConflictEvaluator
import com.fmg.solver.BestSolvers
import com.fmg.solver.BestSolvers.ALL_SOLVERS
import com.fmg.solver.Solver
import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.random.Random

val RANDOM = Random(73)

val scanner = Scanner(System.`in`)


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

fun readRegex(message: String, pattern: Pattern): Matcher {
    println(message)
    while (true) {
        val nextLine = scanner.nextLine()
        val matcher = pattern.matcher(nextLine)
        if (matcher.matches()) {
            return matcher
        }
        println("Invalid pattern!")
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

fun chooseSolver(): Solver {
    return ALL_SOLVERS.values.elementAt(readChoice(ALL_SOLVERS.keys))
}

fun main() {
    when (readChoice(listOf("Step by step", "Solve", "Benchmark", "Test Kronecker"))) {
        0 -> {//Step by step
            val size = askBoardSize()
            printSteps(chooseSolver().createApproximationSequence(size))
        }
        1 -> {//Solution
            val size = askBoardSize()
            printSolution(
                chooseSolver()
                    .createApproximationSequence(size)
                    .take(readInt("Choose a maximum number of steps"))
            )
        }
        2 -> {//Benchmark
            doBenchmark()
        }
        3 -> {//Kronecker test
            testKronecker()
        }
        else -> throw IllegalStateException()
    }

}

private fun testKronecker() {
    val boardGenerator = FactorizerBoardGenerator(BestSolvers.BEST_HILL_CLIMBING_SOLVER)
    var solvedCount = 0
    var unsolvedCount = 0
    for (size in 4..readInt("Choose a limit")) {
        if (TrivialFactorizer.factorize(size, 4).count() > 1) {
            val boards = (0 until 10).map {
                boardGenerator.generateBoard(size)
            }
            solvedCount += boards.count { it.isNQueenSolution() }
            unsolvedCount += boards.count { !it.isNQueenSolution() }
        }
    }
    println(DecimalFormat.getPercentInstance().format(solvedCount.toDouble() / (solvedCount + unsolvedCount)) + " boards solved immediately")
}

private fun doBenchmark() {
    val size = askBoardSize()

    val trials = readInt("Enter the number of trials")
    val limit = readRegex(
        "Choose a maximum number of steps, or type [0-9]s to limit in seconds",
        Pattern.compile("(\\d+)\\s*(s)?")
    )
    val (stepsLimit, timeLimit) = if (limit.group(2).isNullOrEmpty()) {
        limit.group(1).toInt() to null
    } else {
        null to Duration.ofSeconds(limit.group(1).toLong())
    }

    for ((name, solver) in ALL_SOLVERS) {
        val correctnessPercentageStat = DoubleSummaryStatistics()
        val timeStat = DoubleSummaryStatistics()
        val stepsStat = LongSummaryStatistics()
        val conflictsStat = DoubleSummaryStatistics()

        print("Computing using solver $name")
        for (i in 0 until trials) {
            print(".")
            val start = Instant.now()
            val (steps, solution) =
                solver.createApproximationSequence(size)
                    .let { s ->
                        if (timeLimit != null) {
                            s.takeWhile {
                                Duration.between(start, Instant.now()) < timeLimit
                            }
                        } else if (stepsLimit != null) {
                            s.take(stepsLimit)
                        } else {
                            throw IllegalStateException()
                        }
                    }
                    .withIndex()
                    .last()

            timeStat.accept(Duration.between(start, Instant.now()).toNanos() / 1000000000.0)
            stepsStat.accept(steps + 1)
            correctnessPercentageStat.accept(if (solution.isNQueenSolution()) 1.0 else 0.0)
            conflictsStat.accept(TotalConflictEvaluator.evaluate(solution))
        }
        println()
        println(name)
        println("Time statistics: $timeStat")
        println("Steps statistics: $stepsStat")
        println("Conflicts statistics: $conflictsStat")
        println("Correctness probability: ${correctnessPercentageStat.average * 100}%")
        println()
        println()
    }
    enterToExit()
}

private fun askBoardSize(): Int {
    val size = readInt("Enter the board size")
    println("Factors = " + TrivialFactorizer.factorize(size, 4).toList().sorted())
    return size
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
    val start = Instant.now()

    val (steps, solution) = approximationSequence.withIndex().last()

    if (solution.queens.size == solution.size && solution.isNQueenSolution()) {
        println("Solution in $steps steps:")
    } else {
        println("Best approximation after $steps steps:")
        println("Number of conflicts: " + TotalConflictEvaluator.evaluate(solution))
    }
    solution.print()
    println("Took ${Duration.between(start, Instant.now()).toNanos() / 1000000.0} ms")
    enterToExit()
}

private fun enterToExit() {
    println("Enter to exit")
    scanner.nextLine()
}