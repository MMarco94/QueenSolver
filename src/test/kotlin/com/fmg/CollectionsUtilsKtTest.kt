package com.fmg

import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionsUtilsKtTest {

    @Test
    fun repeatLastElement() {
        assertEquals(
            listOf(1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5),
            sequenceOf(1, 2, 3, 4, 5).repeatLastElement().take(12).toList()
        )
        assertEquals(listOf(1, 1, 1, 1), sequenceOf(1).repeatLastElement().take(4).toList())
    }

    @Test
    fun sort() {
        assertEquals(
            listOf(1, 1, 1, 1, 1, 1, 2).sorted(),
            listOf(2, 1, 1, 2, 3, 1, 2, 1, 3, 3, 3, 3, 1, 1, 2, 2).getMinKBy(7) { it }.sorted()
        )
        assertEquals(
            listOf(1, 2, 3, 4).sorted(),
            listOf(5, 6, 4, 1, 2, 3).getMinKBy(4) { it }.sorted()
        )
        assertEquals(
            listOf(1, 1).sorted(),
            listOf(1, 1, 1, 2, 1, 1, 1).getMinKBy(2) { it }.sorted()
        )
        assertEquals(
            listOf(1, 1, 1, 2).sorted(),
            listOf(1, 1, 1, 2).getMinKBy(8) { it }.sorted()
        )

        for (i in 0..10000) {
            val size = RANDOM.nextInt(1000)
            val k = RANDOM.nextInt(1000)
            val arr = mutableListOf<Int>()
            for (j in 0 until size) {
                arr.add(RANDOM.nextInt())
            }
            assertEquals(
                arr.sorted().take(k),
                arr.getMinKBy(k) { it }.sorted()
            )
        }
    }
}