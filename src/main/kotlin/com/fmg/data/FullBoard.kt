package com.fmg.data

/**
 * This class is the basic implementation of Board, which can represent all possible boards
 */
class FullBoard(
    size: Int,
    private val queens: Set<Queen> = emptySet()
) : Board(size) {

    override fun getQueens() = queens
    override fun withQueen(queen: Queen) = FullBoard(size, queens + queen)
    override fun withoutQueen(queen: Queen) = FullBoard(size, queens - queen)
}