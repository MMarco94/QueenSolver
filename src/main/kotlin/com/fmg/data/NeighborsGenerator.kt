package com.fmg.data

import com.fmg.allMinBy
import com.fmg.getMinKBy
import com.fmg.takeWithProbability
import kotlin.math.log2

interface NeighborsGenerator {

    fun generateNeighbors(board: Board): Sequence<Board>
}

/**
 * Generates all the board obtainable by adding a queen to the first free row. Generates n boards
 */
object RowByRowNeighborsGenerator : NeighborsGenerator {

    override fun generateNeighbors(board: Board): Sequence<Board> {
        val firstEmptyRow: Int? = (0 until board.size).asSequence()
            .firstOrNull { row ->
                !board.queensDisposition.hasQueensOnRow(row)
            }

        return if (firstEmptyRow == null) {
            emptySequence()
        } else {
            (0 until board.size).asSequence()
                .map { col -> Queen(firstEmptyRow, col) }
                .map { q -> board.withQueen(q) }
        }
    }
}


/**
 * All the boards obtainable by moving a queen in a row. Generates n^2 boards
 */
object HorizontalQueenMoverNeighborsGenerator : NeighborsGenerator {

    override fun generateNeighbors(board: Board): Sequence<Board> {
        return board.queens.asSequence()
            .flatMap { q ->
                val withoutQueen = board.withoutQueen(q)
                (0 until board.size).asSequence()
                    .filter { col -> col != q.col }
                    .map { col ->
                        withoutQueen.withQueen(Queen(q.row, col))
                    }
            }
    }
}

/**
 * Pre: in the board there must be present at most one queen per row and the number of queens must be greater
 * or equal than K
 *
 * Return all the boards obtainable by moving K queens in a row.
 *
 * The K queens among the others are selected using the queenEvaluator
 */
class KQueensMoverNeighborsGenerator(
    val k: Int,
    val queenEvaluator: QueenEvaluator = TotalQueenConflictEvaluator
) : NeighborsGenerator {

    override fun generateNeighbors(board: Board): Sequence<Board> {
        return recursiveRowDispositions(
            board,
            board.queens
                .asSequence()
                .sortedByDescending { q -> queenEvaluator.evaluate(q, board) }
                .take(k)
                .toList()
        )
    }

    private fun recursiveRowDispositions(board: Board, queensList: List<Queen>): Sequence<Board> {
        return if (queensList.isNotEmpty()) {
            generateBoardsMovingOneQueenInTheCurrentRow(board, queensList.first())
                .flatMap { newBoard ->
                    recursiveRowDispositions(newBoard, queensList.drop(1))
                }
        } else {
            sequenceOf(board)
        }

    }

    private fun generateBoardsMovingOneQueenInTheCurrentRow(board: Board, q: Queen): Sequence<Board> {
        val withoutQueen = board.withoutQueen(q)
        return (0 until board.size).asSequence()
            .map { col ->
                withoutQueen.withQueen(Queen(q.row, col))
            }
    }
}

/**
 * Given a board in which there's only one queen per row and per column, this class generates all the boards by
 * taking the worst log(N) rows, and swapping them with log(N) random rows.
 * Generates log(N)*log(N) boards
 */
class LogRowSwapperNeighborsGenerator(
    val queenEvaluator: QueenEvaluator = TotalQueenConflictEvaluator
) : NeighborsGenerator {
    override fun generateNeighbors(board: Board): Sequence<Board> {
        return board.queens
            .getMinKBy(log2(board.size.toDouble()).toInt() + 1) { q -> -queenEvaluator.evaluate(q, board) }
            .asSequence()
            .flatMap { q1 ->
                board.queens.asSequence()
                    .filter { q2 -> q1 != q2 }
                    .takeWithProbability((log2(board.size.toDouble()).toInt() + 1.0) / board.size)
                    .map { q2 ->
                        board.with(
                            toAddQueens = arrayOf(Queen(q1.row, q2.col), Queen(q2.row, q1.col)),
                            toRemoveQueens = arrayOf(q1, q2)
                        )
                    }
            }
    }
}

class SimulatedAnnealingNeighborsGenerator(
    val queenEvaluator: QueenEvaluator = TotalQueenConflictEvaluator
) : NeighborsGenerator {
    override fun generateNeighbors(board: Board): Sequence<Board> {
        return board.queens
            .getMinKBy(log2(board.size.toDouble()).toInt() + 1) { q -> -queenEvaluator.evaluate(q, board) }
            .asSequence()
            .flatMap { q1 ->
                board.queens.asSequence()
                    .filter { q2 -> q1 != q2 }
                    .map { q2 ->
                        board.with(
                            toAddQueens = arrayOf(Queen(q1.row, q2.col), Queen(q2.row, q1.col)),
                            toRemoveQueens = arrayOf(q1, q2)
                        )
                    }
            }
    }
}

class RowSwapperNeighborsGenerator(
    val queenEvaluator: QueenEvaluator = TotalQueenConflictEvaluator
) : NeighborsGenerator {
    override fun generateNeighbors(board: Board): Sequence<Board> {
        return board.queens
            .asSequence()
            .flatMap { q1 ->
                board.queens
                    .asSequence()
                    .filter { q2 -> q1 != q2 }
                    .map { q2 ->
                        board.with(
                            toAddQueens = arrayOf(Queen(q1.row, q2.col), Queen(q2.row, q1.col)),
                            toRemoveQueens = arrayOf(q1, q2)
                        )
                    }
            }
    }
}

class RowSwapperWithQueenHeuristicNeighborsGenerator(
    val queenEvaluator: QueenEvaluator = TotalQueenConflictEvaluator
) : NeighborsGenerator {
    override fun generateNeighbors(board: Board): Sequence<Board> {
        return board.queens
            .asSequence()
            .allMinBy { q -> -queenEvaluator.evaluate(q, board) }
            .asSequence()
            .flatMap { q1 ->
                board.queens
                    .asSequence()
                    .filter { q2 -> q1 != q2 }
                    .map { q2 ->
                        board.with(
                            toAddQueens = arrayOf(Queen(q1.row, q2.col), Queen(q2.row, q1.col)),
                            toRemoveQueens = arrayOf(q1, q2)
                        )
                    }
            }
    }
}



