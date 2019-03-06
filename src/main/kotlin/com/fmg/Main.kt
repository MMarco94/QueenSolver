package com.fmg

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
    val board1 = FullBoard(size)
    val board2 = RowByRowBoard(size)
    val board = board2

    //10 = 125072
    ConstraintPropagationAndBacktrackingSolver(board, size)
        .createSolveSteps()
        .forEachIndexed { step, partialSolution ->
            println("Step $step:")
            partialSolution.print()
            println()
        }

    println("Created $boardsCreated boards")
}
