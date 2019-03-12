package com.fmg

import org.junit.Assert.assertEquals
import org.junit.Test

class SequenceUtilsKtTest {

    @Test
    fun repeatLastElement() {
        assertEquals(
            listOf(1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5),
            sequenceOf(1, 2, 3, 4, 5).repeatLastElement().take(12).toList()
        )
        assertEquals(listOf(1, 1, 1, 1), sequenceOf(1).repeatLastElement().take(4).toList())
    }
}