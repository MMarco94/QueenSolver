package com.fmg


interface Factorizer {
    fun factorize(n : Int) : Sequence<Int>
}

object TrivialFactorizer : Factorizer {
    override fun factorize(n: Int): Sequence<Int> {
        var factors = ArrayList<Int>()
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

        while (factors[0] == 2) {
            var a = factors[0]
            var b = factors[1]
            factors.removeAt(0)
            factors.removeAt(0)
            factors.add(a * b)
        }

        return factors.asSequence()
    }

}