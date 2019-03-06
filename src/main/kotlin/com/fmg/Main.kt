package com.fmg

import com.fmg.data.Board
import com.fmg.data.RowByRowBoard
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
        val board = RowByRowBoard(size)

        val start = System.nanoTime()

        val solver = ConstraintPropagationAndBacktrackingSolver(board, size)
        val steps = solver.createSolveSteps()

        val numberOfSteps = steps.count()

        timeStat.accept((System.nanoTime() - start) / 1000000000.0)
        stepsStat.accept(numberOfSteps)

        //printSolution(steps)
        //printSteps(steps)
    }

    println("Time statistics: $timeStat")
    println("Steps statistics: $stepsStat")
}

private fun printSteps(steps: Sequence<Board>) {
    steps.forEachIndexed { step, partialSolution ->
        println("Step $step:")
        partialSolution.print()
        println()
        println("Press enter to continue")
        scanner.nextLine()
    }
    println("Solved!!!")
}

private fun printSolution(steps: Sequence<Board>) {
    val solution = steps.withIndex().last()
    println("Solution in ${solution.index} steps:")
    solution.value.print()
}
