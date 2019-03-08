package com.fmg.data

/**
 * This class represent a Queen on a particular position
 */
data class Queen(
    val row: Int,
    val col: Int
) {
    val descendingDiagonalId = row - col
    val ascendingDiagonalId = row + col
}