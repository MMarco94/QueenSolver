package com.fmg.data

class FastQueenSet private constructor(
    private val queens: Array<Queen>
) : AbstractSet<Queen>() {

    constructor() : this(emptyArray())

    override val size = queens.size

    override fun contains(element: Queen) = queens.binarySearch(element) >= 0

    override fun iterator() = queens.iterator()

    /**
     * Adds and removes queens at the same time
     *
     * @param toAddQueens the queens to add. They must be new queens
     * @param toRemoveQueens the queens to remove. They must be in this set
     */
    fun with(
        toAddQueens: Array<Queen> = emptyArray(),
        toRemoveQueens: Array<Queen> = emptyArray()
    ): FastQueenSet {
        return if (queens.isEmpty() && toRemoveQueens.isEmpty()) {
            FastQueenSet(toAddQueens.sortedArray())
        } else {
            var ret = this
            for (queen in toAddQueens) {
                ret = ret.withQueen(queen)
            }
            for (queen in toRemoveQueens) {
                ret = ret.withoutQueen(queen)
            }
            ret
        }
    }

    fun withQueen(queen: Queen): FastQueenSet {
        val insertionPoint = -queens.binarySearch(queen) - 1
        if (insertionPoint < 0) throw IllegalArgumentException("Queen already present")

        val newQueens = queens.copyOf(queens.size + 1)
        newQueens[insertionPoint] = queen
        System.arraycopy(
            queens, insertionPoint,
            newQueens, insertionPoint + 1,
            queens.size - insertionPoint
        )

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