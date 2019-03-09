package com.fmg.data

import com.fmg.RANDOM
import java.util.*

interface BoardGenerator {
    fun generateBoard(size: Int): Board
}

object EmptyBoardGenerator : BoardGenerator {
    override fun generateBoard(size: Int) = Board(size)
}

object RandomBoardGenerator : BoardGenerator {
    override fun generateBoard(size: Int): Board {
        val rnd = RANDOM
        return generateSequence(Board(size)) { prev ->
            if (prev.queens.size == size) {
                null
            } else {
                prev.withQueen(Queen(rnd.nextInt(size), rnd.nextInt(size)))
            }
        }.last()
    }
}

object OneQueenPerRowRandomBoardGenerator : BoardGenerator {
    override fun generateBoard(size: Int): Board {
        val rnd = RANDOM
        return generateSequence(Board(size)) { prev ->
            if (prev.queens.size == size) {
                null
            } else {
                prev.withQueen(Queen(prev.queens.size, rnd.nextInt(size)))
            }
        }.last()
    }
}