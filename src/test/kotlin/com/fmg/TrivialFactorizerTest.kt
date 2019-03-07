package com.fmg

import org.junit.Test

import org.junit.Assert.*

class TrivialFactorizerTest {

    @Test
    fun factorize() {
        assertEquals(listOf(2, 2, 3), TrivialFactorizer.factorize(12).toList())
        assertEquals(listOf(13), TrivialFactorizer.factorize(13).toList())
    }
}