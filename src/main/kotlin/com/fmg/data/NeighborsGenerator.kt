package com.fmg.data

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
                board.queens.none { q -> q.row == row }
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
 * All the boards obtainable by moving a queen. Generates n^3 boards
 */
object QueenMoverNeighborsGenerator : NeighborsGenerator {

    override fun generateNeighbors(board: Board): Sequence<Board> {
        return board.queens.asSequence()
            .flatMap { q ->
                val without = board.withoutQueen(q)
                Board.generateAllQueens(board.size)
                    .filterNot { q2 -> q2 in board.queens }
                    .map { q2 -> without.withQueen(q2) }
            }
    }
}

/**
 * All the board obtainable by moving the queen with more conflicts. Generates n^2 boards
 */
object MQueenMoverNeighborsGenerator : NeighborsGenerator {

    override fun generateNeighbors(board: Board): Sequence<Board> {
        val queenWithMoreConflicts = board.queens.asSequence()
            .maxBy { q ->
                board.queens.count { q2 -> q2 != q && q2.conflicts(q) }
            }!!
        val without = board.withoutQueen(queenWithMoreConflicts)
        return Board.generateAllQueens(board.size)
            .filterNot { q2 -> q2 in board.queens }
            .map { q2 -> without.withQueen(q2) }
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