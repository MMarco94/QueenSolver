package com.fmg

import com.fmg.data.UnconstrainedBoard
import com.fmg.data.ConstrainedBoard
import com.fmg.solver.ConstraintPropagationAndBacktrackingSolver
import java.util.*

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

fun main() {
    val size = readInt("Enter the board size")
    val trials = readInt("Enter the number of trials")

    val timeStat = DoubleSummaryStatistics()
    val stepsStat = LongSummaryStatistics()

    for (i in 0 until trials) {
        println("Computing trial n° $i...")
        val board = ConstrainedBoard(size)

        val start = System.nanoTime()

        val solver = ConstraintPropagationAndBacktrackingSolver(board, size)
        val steps = solver.createSolveSteps()

        //printSteps(steps)

        val numberOfSteps = steps.count()

        timeStat.accept((System.nanoTime() - start) / 1000000000.0)
        stepsStat.accept(numberOfSteps)

        //printSolution(steps)

    }

    println("Time statistics: $timeStat")
    println("Steps statistics: $stepsStat \n\n")


    //Unconstrained version

    val timeStat2 = DoubleSummaryStatistics()
    val stepsStat2 = LongSummaryStatistics()

    for (i in 0 until trials) {
        println("Computing trial n° $i...")
        val board = UnconstrainedBoard(size)

        val start = System.nanoTime()

        val solver = ConstraintPropagationAndBacktrackingSolver(board, size)
        val steps = solver.createSolveSteps()

        //printSteps(steps)

        val numberOfSteps = steps.count()

        timeStat2.accept((System.nanoTime() - start) / 1000000000.0)
        stepsStat2.accept(numberOfSteps)

        //printSolution(steps)

    }

    println("Time statistics: $timeStat2")
    println("Steps statistics: $stepsStat2")
}

private fun printSteps(steps: Sequence<UnconstrainedBoard>) {
    steps.forEachIndexed { step, partialSolution ->
        println("Step $step:")
        partialSolution.print()
        println()
        println("Press enter to continue")
        scanner.nextLine()
    }
    println("Solved!!!")
}

private fun printSolution(steps: Sequence<UnconstrainedBoard>) {
    val solution = steps.withIndex().last()
    println("Solution in ${solution.index} steps:")
    solution.value.print()
}
