package com.fmg.data

class FastQueenSet private constructor(
    private val queens: Array<Queen>
) : AbstractSet<Queen>() {

    constructor() : this(emptyArray())

    override val size = queens.size

    override fun contains(element: Queen) = queens.binarySearch(element) >= 0

    override fun iterator() = queens.iterator()

    fun withQueen(queen: Queen): FastQueenSet {
        val insertionPoint = -queens.binarySearch(queen) - 1
        if (insertionPoint < 0) throw IllegalArgumentException("Queen already present")

        val newQueens = queens.copyOf(queens.size + 1)
        newQueens[insertionPoint] = queen
        for (i in insertionPoint + 1 until newQueens.size) {
            newQueens[i] = queens[i - 1]
        }

        @Suppress("UNCHECKED_CAST")
        return FastQueenSet(newQueens as Array<Queen>)
    }

    fun withoutQueen(queen: Queen): FastQueenSet {
        val indexOf = queens.binarySearch(queen)
        if (indexOf < 0) throw IllegalArgumentException("Queen not present")

        return FastQueenSet(Array(queens.size - 1) { index ->
            if (index < indexOf) {
                queens[index]
            } else {
                queens[index + 1]
            }
        })
    }
}