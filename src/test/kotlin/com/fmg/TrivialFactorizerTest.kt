package com.fmg

import org.junit.Assert.assertEquals
import org.junit.Test

class TrivialFactorizerTest {

    @Test
    fun factorize() {
        assertEquals(listOf(3, 4), TrivialFactorizer.factorize(12).toList())
        assertEquals(listOf(13), TrivialFactorizer.factorize(13).toList())
        assertEquals(listOf(4, 6), TrivialFactorizer.factorize(24).toList())
        assertEquals(listOf(4, 8), TrivialFactorizer.factorize(32).toList())
        assertEquals(listOf(3, 4, 6), TrivialFactorizer.factorize(72).toList())
        assertEquals(listOf(8, 7), TrivialFactorizer.factorize(56).toList())
    }
}