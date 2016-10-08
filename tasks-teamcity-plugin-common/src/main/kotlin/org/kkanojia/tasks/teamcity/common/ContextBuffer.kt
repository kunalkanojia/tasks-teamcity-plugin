package org.kkanojia.tasks.teamcity.common

internal class ContextBuffer(val bufferSize: Int) {
    private val buffer: Array<String?>

    private var index = 0
    var isFull = false
        private set

    init {
        this.buffer = arrayOfNulls<String>(bufferSize)
    }

    fun push(line: String) {
        // adding value
        buffer.set(index, line)

        // fixing index and full
        index++
        if (index == bufferSize) {
            isFull = true
            index = 0
        }
    }

    fun getAt(pos: Int): String {
        assert(pos < 0)

        val bufferIndex = (index + pos + bufferSize) % bufferSize
        return buffer[bufferIndex].toString()
    }

    val bufferReversed: Array<String?>
        get() {
            val result = arrayOfNulls<String>(bufferSize)

            for (i in 0..bufferSize - 1) {
                result[i] = getAt(-i - 1)
            }

            return result
        }

    fun getBuffer(): Array<String?> {
        val result = arrayOfNulls<String>(bufferSize)

        for (i in 0..bufferSize - 1) {
            result[i] = getAt(-bufferSize + i)
        }

        return result
    }

    fun reset() {
        index = 0
        isFull = false
        for (i in 0..bufferSize - 1) {
            buffer[i] = null
        }
    }
}
