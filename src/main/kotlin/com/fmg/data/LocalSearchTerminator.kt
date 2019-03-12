package com.fmg.data

import com.fmg.takeWhileInclusive
import com.fmg.terminate

interface LocalSearchTerminator {
    fun terminate(boardSequence: Sequence<BoardWithScore>): Sequence<BoardWithScore>
}

object TerminateWhenSolved : LocalSearchTerminator {
    override fun terminate(boardSequence: Sequence<BoardWithScore>): Sequence<BoardWithScore> {
        return boardSequence.takeWhileInclusive { !it.board.isNQueenSolution() }
    }
}

object TerminateWhenNotImproving : LocalSearchTerminator {
    override fun terminate(boardSequence: Sequence<BoardWithScore>): Sequence<BoardWithScore> {
        //I have to add a last null element, since "zipWithNext" doesn't return the last element of the sequence, since it doesn't have a next
        return (boardSequence + sequenceOf(null as BoardWithScore?))
            .zipWithNext { b1, b2 ->
                b1 to (b2 == null || b2.score < b1!!.score)
            }
            .takeWhileInclusive { (_, isImproving) -> isImproving }
            .map { it.first!! }
    }
}

class TerminateComposition(val terminators: Set<LocalSearchTerminator>) : LocalSearchTerminator {
    override fun terminate(boardSequence: Sequence<BoardWithScore>): Sequence<BoardWithScore> {
        var retSequence = boardSequence
        for (t in terminators) {
            retSequence = t.terminate(retSequence)
        }
        return retSequence
    }

}

fun main() {
    val b = Board(10)
    sequenceOf(
        b.withScore(10.0),
        b.withScore(9.0),
        b.withScore(8.0),
        b.withScore(7.0),
        b.withScore(7.0)
    ).terminate(TerminateWhenNotImproving)
        .forEach { (_, score) ->
            println("Score $score")
        }
}