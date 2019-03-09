package com.fmg.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FastQueenSetTest {

    @Test
    fun test() {
        val set = FastQueenSet()
        assertEquals(0, set.size)

        val set2 = set.withQueen(Queen(0, 0))
        assertEquals(1, set2.size)
        assertEquals(listOf(Queen(0, 0)), set2.toList())

        val set3 = set2.withQueen(Queen(1, 1)).withQueen(Queen(10, 11)).withQueen(Queen(5, 6))
        assertEquals(4, set3.size)
        assertEquals(listOf(Queen(0, 0), Queen(1, 1), Queen(5, 6), Queen(10, 11)), set3.toList())
        assertTrue(set3.contains(Queen(5, 6)))
        assertTrue(set3.contains(Queen(10, 11)))
        assertFalse(set3.contains(Queen(11, 11)))

        val set4 = set3.withoutQueen(Queen(1, 1))
        assertEquals(3, set4.size)
        assertEquals(listOf(Queen(0, 0), Queen(5, 6), Queen(10, 11)), set4.toList())
        assertFalse(set4.contains(Queen(1, 1)))


        var fast = FastQueenSet()
        var control = setOf<Queen>()

        val rnd = Random()
        for (i in 0..10000) {
            val queen = Queen(rnd.nextInt(20), rnd.nextInt(20))
            if (control.contains(queen)) {
                assertTrue(fast.contains(queen))
            } else {
                assertFalse(fast.contains(queen))
                fast = fast.withQueen(queen)
                control = control + queen
            }
            assertEquals(control, fast)
        }
    }


}