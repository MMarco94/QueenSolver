package com.fmg


interface Factorizer {
    fun factorize(n : Int) : Sequence<Int>
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
        return factors.asSequence()
    }

}