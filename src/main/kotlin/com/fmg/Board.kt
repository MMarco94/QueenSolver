package com.fmg

import kotlin.math.abs

var boardCreated = 0

class Board(
    val size: Int,
    val queens: Set<Queen> = emptySet()
) {
    init {
        boardCreated++
    }

    fun withQueen(queen: Queen) = Board(size, queens + queen)

    fun withoutQueen(queen: Queen) = Board(size, queens - queen)

    fun move(previous: Queen, new: Queen) = Board(size, queens - previous + new)

    fun isValid(): Boolean {
        return queens.none { queen ->
            queens.any { q -> queen != q && queen.conflicts(q) }
        }
    }

    fun getPossibleMoves() = generateSequence(Queen(0, 0)) { prev ->
        when {
            prev.col < size - 1 -> Queen(prev.row, prev.col + 1)
            prev.row < size - 1 -> Queen(prev.row + 1, 0)
            else -> null
        }
    }.filterNot { q -> q in queens }

    fun getValidMoves() = getPossibleMoves().filter { q ->
        queens.none { q2 -> q2.conflicts(q) }
    }

    fun print() {
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (queens.contains(Queen(r, c))) {
                    print("x")
                } else {
                    print(" ")
                }
                print("|")
            }
            println()
        }
    }
}

data class Queen(
    val row: Int,
    val col: Int
) {

    fun conflicts(another: Queen): Boolean {
        return row == another.row || col == another.col || abs(row - another.row) == abs(col - another.col)
    }
}