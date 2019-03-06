package com.fmg.data

import kotlin.math.abs

/**
 * This class represent a Queen on a particular position
 */
data class Queen(
    val row: Int,
    val col: Int
) {
    fun conflicts(another: Queen): Boolean {
        return row == another.row || col == another.col || abs(row - another.row) == abs(col - another.col)
    }
}