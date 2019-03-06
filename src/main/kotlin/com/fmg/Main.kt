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
    val board = RowByRowBoard(size)

    val solver = ConstraintPropagationAndBacktrackingSolver(board, size)
    val steps = solver.createSolveSteps()

    printSolution(steps)
    //printSteps(steps)
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
