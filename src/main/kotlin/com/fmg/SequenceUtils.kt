package com.fmg

fun <T> Sequence<T>.shuffled() = toList().shuffled(RANDOM).asSequence()

/**
 * @return All the elements that minimize the given function
 */
fun <T, R : Comparable<R>> Sequence<T>.allMinBy(selector: (T) -> R): List<T> {
    return fold<T, Pair<R?, MutableList<T>>>(null to mutableListOf()) { (prevFitness, minBoards), element ->
        val fitness = selector(element)
        when {
            prevFitness == null || fitness < prevFitness -> fitness to mutableListOf(element)
            fitness == prevFitness -> {
                minBoards.add(element)
                fitness to minBoards
            }
            else -> prevFitness to minBoards
        }
    }.second
}

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
