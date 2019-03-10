package com.fmg.data

import com.fmg.shuffled
import kotlin.math.log2

interface NeighborsGenerator {

    fun generateNeighbors(board: Board): Sequence<Board>
}


class NeighborsGeneratorsUnion(val generators: Iterable<NeighborsGenerator>) : NeighborsGenerator {
    override fun generateNeighbors(board: Board): Sequence<Board> {
        return generators.asSequence().flatMap { g ->
            g.generateNeighbors(board)
        }
    }
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
            emptySequence()//TODO: board rimuovendo regine?
        } else {
            (0 until board.size).asSequence()
                .map { col -> Queen(firstEmptyRow, col) }
                .map { q -> board.withQueen(q) }
        }
    }
}

/**
 * All the board obtainable by moving one of the two queens with more conflicts. Generates 2 * n^2 boards
 */
object TwoQueenMoverNeighborsGenerator : NeighborsGenerator {

    override fun generateNeighbors(board: Board): Sequence<Board> {
        return board.queens
            .sortedByDescending { q ->
                board.queensDisposition.countConflicts(q)
            }
            .asSequence()
            .take(2)
            .flatMap { q ->
                val withoutQueen = board.withoutQueen(q)
                Board.generateAllQueens(board.size)
                    .filter { q2 -> q2 !in board.queens }
                    .map { q2 -> withoutQueen.withQueen(q2) }
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
 * All the boards obtainable by removing a queen. Generates n boards
 */
object QueenRemoverNeighborsGenerator : NeighborsGenerator {

    override fun generateNeighbors(board: Board): Sequence<Board> {
        return board.queens.asSequence().map { q ->
            board.withoutQueen(q)
        }
    }
}

/**
 * Pre: in the board there must be present at most one queen per row and the number of queens must be greater or equal of K
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
                .sortedBy { q -> queenEvaluator.evaluate(q, board) }
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
        return board.queens.asSequence()
            .sortedBy { q -> queenEvaluator.evaluate(q, board) }
            .take(log2(board.size.toDouble()).toInt() + 1)
            .flatMap { q1 ->
                val firstRow = q1.row
                val withoutQueen = board.withoutQueen(q1)
                (firstRow + 1 until board.size).asSequence()
                    .shuffled()
                    .take(log2(board.size.toDouble()).toInt() + 1)
                    .map { secondRow ->
                        val q2 = board.queens.single { q -> q.row == secondRow }
                        withoutQueen
                            .withoutQueen(q2)
                            .withQueen(Queen(q1.row, q2.col))
                            .withQueen(Queen(q2.row, q1.col))
                    }
            }
    }
}