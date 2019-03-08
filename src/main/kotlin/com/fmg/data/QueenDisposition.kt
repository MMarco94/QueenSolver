package com.fmg.data

/**
 * A class that holds queens. It also keeps statistics about the dispositions of the queens on the board, to make most operations O(1)
 */
class QueenDisposition private constructor(
    val queens: Set<Queen>,
    val rows: Map<Int, Set<Queen>>,
    val columns: Map<Int, Set<Queen>>,
    val descendingDiagonals: Map<Int, Set<Queen>>,
    val ascendingDiagonals: Map<Int, Set<Queen>>
) {

    constructor() : this(emptySet(), emptyMap(), emptyMap(), emptyMap(), emptyMap())

    fun countConflicts(): Int {
        return rows.values.sumBy { r -> r.size * (r.size - 1) / 2 } +
                columns.values.sumBy { c -> c.size * (c.size - 1) / 2 } +
                descendingDiagonals.values.sumBy { dd -> dd.size * (dd.size - 1) / 2 } +
                ascendingDiagonals.values.sumBy { ad -> ad.size * (ad.size - 1) / 2 }
    }

    fun hasConflicts() = countConflicts() > 0

    fun countConflicts(queen: Queen): Int {
        var ret = 0
        ret += rows[queen.row]?.size ?: 0
        ret += columns[queen.col]?.size ?: 0
        ret += descendingDiagonals[queen.descendingDiagonalId]?.size ?: 0
        ret += ascendingDiagonals[queen.ascendingDiagonalId]?.size ?: 0
        if (queen in queens) {
            ret -= 4
        }
        return ret
    }

    fun hasConflicts(queen: Queen): Boolean = countConflicts(queen) > 0

    fun rowCountConflicts(row: Int): Int {
        val r = rows[row]?.size ?: 0
        return r * (r - 1) / 2
    }

    fun rowHasConflicts(row: Int) = rowCountConflicts(row) > 0

    fun columnCountConflicts(column: Int): Int {
        val r = columns[column]?.size ?: 0
        return r * (r - 1) / 2
    }

    fun columnHasConflicts(column: Int) = columnCountConflicts(column) > 0


    fun withQueen(queen: Queen): QueenDisposition {
        return if (queen in queens) {
            this //This disposition already contains the given queen
        } else {
            QueenDisposition(
                queens + queen,
                rows.toMutableMap().also { m ->
                    m[queen.row] = m.getOrElse(queen.row) { HashSet(0) }.plus(queen)
                },
                columns.toMutableMap().also { m ->
                    m[queen.col] = m.getOrElse(queen.col) { HashSet(0) }.plus(queen)
                },
                descendingDiagonals.toMutableMap().also { m ->
                    m[queen.descendingDiagonalId] = m.getOrElse(queen.descendingDiagonalId) { HashSet(0) }.plus(queen)
                },
                ascendingDiagonals.toMutableMap().also { m ->
                    m[queen.ascendingDiagonalId] = m.getOrElse(queen.ascendingDiagonalId) { HashSet(0) }.plus(queen)
                }
            )
        }
    }

    fun withoutQueen(queen: Queen): QueenDisposition {
        if (queen !in queens) {
            throw IllegalArgumentException()
        }
        return QueenDisposition(
            queens - queen,
            rows.toMutableMap().also { m ->
                m[queen.row] = m.getValue(queen.row).minus(queen)
            },
            columns.toMutableMap().also { m ->
                m[queen.col] = m.getValue(queen.col).minus(queen)
            },
            descendingDiagonals.toMutableMap().also { m ->
                m[queen.descendingDiagonalId] = m.getValue(queen.descendingDiagonalId).minus(queen)
            },
            ascendingDiagonals.toMutableMap().also { m ->
                m[queen.ascendingDiagonalId] = m.getValue(queen.ascendingDiagonalId).minus(queen)
            }
        )
    }
}