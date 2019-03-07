package com.fmg.data

/**
 * This class represents a chess board size x size.
 *
 * Any board representable by this class MUST be obtainable by subsequent calls to getNeighbors
 */
open class FullBoard(
    size: Int,
    queens: Set<Queen> = emptySet()
) : Board(size, queens) {

    /**
     * @return a new board that contains the new given queen
     */
    override fun withQueen(queen: Queen) = FullBoard(size, queens + queen)

    /**
     * @return a new board without the given queen
     */
    override fun withoutQueen(queen: Queen) = FullBoard(size, queens - queen)

    override fun getNeighbors(): Sequence<Board> {
        return if (queens.size == size) {
            //I'm a full board. My neighbors are board with one less queen
            queens.asSequence().map { q -> withoutQueen(q) }
        } else {
            generateSequence(Queen(0, 0)) { prev ->
                when {
                    prev.col < size - 1 -> Queen(prev.row, prev.col + 1)
                    prev.row < size - 1 -> Queen(prev.row + 1, 0)
                    else -> null
                }
            }
                .filterNot { q -> q in queens }
                .map { q -> withQueen(q) }
        }
    }
}