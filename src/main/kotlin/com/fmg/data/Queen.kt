package com.fmg.data

/**
 * This class represent a Queen on a particular position
 */
data class Queen(
    val row: Int,
    val col: Int
) : Comparable<Queen> {
    override fun compareTo(other: Queen): Int {
        return if (other.row == row) {
            col.compareTo(other.col)
        }else{
            row.compareTo(other.row)
        }
    }

    /**
     * A number between 0 and 1n (inclusive) that identifies the descending diagonal in which the queen is
     */
    val descendingDiagonalId = if (row < col) {
        (col - row) * 2 - 1
    } else {
        (row - col) * 2
    }
    /**
     * A number between 0 and 1n (inclusive) that identifies the ascending diagonal in which the queen is
     */
    val ascendingDiagonalId = row + col
}


fun main() {
    println("Descending diagonal id")
    for (r in 0..8) {
        for (c in 0..8) {
            print(Queen(r, c).descendingDiagonalId)
            print("\t")
        }
        println()
    }
    println()
    println("Ascending diagonal id")
    for (r in 0..8) {
        for (c in 0..8) {
            print(Queen(r, c).ascendingDiagonalId)
            print("\t")
        }
        println()
    }
}