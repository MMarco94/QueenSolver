package com.fmg

fun <T> Sequence<T>.shuffled() = toList().shuffled().asSequence()

/**
 * @return a sequence that ends when the given predicate becomes false. The first element that doesn't match the predicate is returned
 */
fun <T> Sequence<T>.takeWhileInclusive(predicate: (T) -> Boolean): Sequence<T> {
    val wrapped = this.iterator()
    return object : Sequence<T> {
        private var shouldContinue = true

        override fun iterator(): Iterator<T> {
            return object : Iterator<T> {
                override fun hasNext() = shouldContinue && wrapped.hasNext()

                override fun next(): T {
                    val ret = wrapped.next()
                    if (!predicate(ret)) {
                        shouldContinue = false
                    }
                    return ret
                }
            }
        }

    }
}
