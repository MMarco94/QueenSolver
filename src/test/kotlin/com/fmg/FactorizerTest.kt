package com.fmg

import org.junit.Assert.assertEquals
import org.junit.Test

class FactorizerTest {

    @Test
    fun factorize() {
        testFactorizer(TrivialFactorizer)
    }

    fun testFactorizer(factorizer: Factorizer) {
        assertEquals(listOf(4), factorizer.factorize(4, 4).toList())
        assertEquals(listOf(4, 8), factorizer.factorize(32, 4).toList())
        assertEquals(listOf(12), factorizer.factorize(12, 4).toList())
        assertEquals(listOf(13), factorizer.factorize(13, 4).toList())
        assertEquals(listOf(4, 6), factorizer.factorize(24, 4).toList())
        assertEquals(listOf(4, 8), factorizer.factorize(32, 4).toList())
        //assertEquals(listOf(8, 9), factorizer.factorize(72, 4).toList())
        assertEquals(listOf(7, 8), factorizer.factorize(56, 4).toList())
        assertEquals(listOf(13, 1283, 6), factorizer.factorize(100_074, 4).toList())

    }
}