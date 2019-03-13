package com.fmg

import com.fmg.data.BoardWithScore
import com.fmg.data.LocalSearchTerminator

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
    val wrapped = this
    return object : Sequence<T> {

        override fun iterator(): Iterator<T> {
            val wrappedIterator = wrapped.iterator()
            return object : Iterator<T> {
                private var shouldContinue = true
                override fun hasNext() = shouldContinue && wrappedIterator.hasNext()

                override fun next(): T {
                    val ret = wrappedIterator.next()
                    if (!predicate(ret)) {
                        shouldContinue = false
                    }
                    return ret
                }
            }
        }

    }
}

fun <T> Sequence<T>.repeatLastElement(fallbackIfEmpty: () -> T = { throw UnsupportedOperationException("Empty sequence cannot be extended!") }): Sequence<T> {
    val wrapped = this
    return object : Sequence<T> {

        override fun iterator(): Iterator<T> {
            val wrappedIterator = wrapped.iterator()
            return object : Iterator<T> {
                var lastElement: T? = null
                override fun hasNext() = true

                override fun next(): T {
                    if (wrappedIterator.hasNext()) {
                        lastElement = wrappedIterator.next()
                    } else if (lastElement == null) {
                        lastElement = fallbackIfEmpty()
                    }
                    return lastElement!!
                }

            }
        }
    }
}

fun Sequence<BoardWithScore>.terminate(localSearchTerminator: LocalSearchTerminator) =
    localSearchTerminator.terminate(this)

fun <T : Any> Sequence<T>.extractWithRepetitions(): Sequence<T> {
    val toList = this.toList()
    return generateSequence { toList[RANDOM.nextInt(toList.size)] }
}