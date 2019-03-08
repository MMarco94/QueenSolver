package com.fmg


interface Factorizer {
    fun factorize(n: Int): Sequence<Int>
}

object TrivialFactorizer : Factorizer {
    override fun factorize(n: Int): Sequence<Int> {
        val factors = ArrayList<Int>()
        var x = n
        var i = 2
        while (i <= x) {
            if ((x % i) == 0) {
                factors.add(i)
                x /= i
            } else {
                i++
            }
        }

        while (factors[0] == 2 || factors[0] == 3) {
            val a = factors[0]
            val b = factors[1]
            factors.removeAt(0)
            factors.removeAt(0)
            val c = factors.lastOrNull { it < a * b }
            if (c != null) {
                factors.add(factors.lastIndexOf(c) + 1, a * b)
            } else {
                factors.add(a * b)
            }
        }

        return factors.asSequence()
    }

}