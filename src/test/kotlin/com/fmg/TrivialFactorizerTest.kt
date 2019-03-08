package com.fmg

import org.junit.Assert.assertEquals
import org.junit.Test

class TrivialFactorizerTest {

    @Test
    fun factorize() {
        assertEquals(listOf(12), TrivialFactorizer.factorize(12).toList())
        assertEquals(listOf(13), TrivialFactorizer.factorize(13).toList())
        assertEquals(listOf(4, 6), TrivialFactorizer.factorize(24).toList())
        assertEquals(listOf(4, 8), TrivialFactorizer.factorize(32).toList())
        assertEquals(listOf(8, 9), TrivialFactorizer.factorize(72).toList())
        assertEquals(listOf(7, 8), TrivialFactorizer.factorize(56).toList())
    }
}