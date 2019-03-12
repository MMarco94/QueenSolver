package com.fmg.data

import com.fmg.takeWhileInclusive

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
        return boardSequence
            .zipWithNext { b1, b2 ->
                b1 to (b2.score < b1.score)
            }
            .takeWhileInclusive { (_, isImproving) -> isImproving }
            .map { it.first }
    }
}

class TerminateComposition(val terminators : Set<LocalSearchTerminator>) : LocalSearchTerminator {
    override fun terminate(boardSequence: Sequence<BoardWithScore>): Sequence<BoardWithScore> {
        var retSequence = boardSequence
        for (t in terminators) {
            retSequence = t.terminate(retSequence)
        }
        return retSequence
    }

}