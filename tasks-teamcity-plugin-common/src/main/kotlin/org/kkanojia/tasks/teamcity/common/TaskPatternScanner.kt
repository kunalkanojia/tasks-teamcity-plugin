package org.kkanojia.tasks.teamcity.common

import com.ibm.icu.text.CharsetDetector
import org.mozilla.universalchardet.UniversalDetector
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.MalformedInputException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*
import java.util.regex.Pattern


class TaskPatternScanner @JvmOverloads constructor(
        minors: List<String>,
        majors: List<String>,
        criticals: List<String>,
        private val contextBefore: Int = 0,
        private val contextAfter: Int = 0) {

    private val minors: Pattern
    private val majors: Pattern
    private val criticals: Pattern

    private val detector = UniversalDetector(null)

    init {
        this.minors = buildPattern(minors)
        this.majors = buildPattern(majors)
        this.criticals = buildPattern(criticals)
    }

    private fun buildPattern(regexes: List<String>): Pattern {
        val sb = StringBuilder()
        var first = true
        for (regex in regexes) {
            if (first) {
                first = false
            } else {
                sb.append('|')
            }
            sb.append(regex)
        }
        return Pattern.compile(sb.toString())
    }

    @Throws(IOException::class)
    private fun guessCharsetChardet(file: Path, originalAttempt: Charset): Charset? {
        var detectedCharset: String? = null
        try {
            Files.newByteChannel(file, StandardOpenOption.READ).use { byteChannel ->
                val buffer = ByteBuffer.allocate(16384)
                var count: Int = byteChannel.read(buffer)
                while (count > 0 && !detector.isDone) {
                    detector.handleData(buffer.array(), 0, count)
                    count = byteChannel.read(buffer)
                }
            }
        } finally {
            detector.dataEnd()
            detectedCharset = detector.detectedCharset
        }
        if (detectedCharset != null)
            return Charset.forName(detector.detectedCharset)
        else if (originalAttempt === StandardCharsets.UTF_8)
            return StandardCharsets.US_ASCII
        else if (originalAttempt === StandardCharsets.US_ASCII)
            return StandardCharsets.ISO_8859_1
        else return null
    }

    @Throws(IOException::class)
    private fun guessCharset(file: Path, charset: Charset): Charset {

        val detector = CharsetDetector()
        var data: ByteArray? = null

        Files.newByteChannel(file, StandardOpenOption.READ).use { byteChannel ->
            val size = byteChannel.size()

            if (size >= Integer.MAX_VALUE) {
                return guessCharsetChardet(file, charset)!!
            }

            val smallsize = size.toInt()
            val buffer = ByteBuffer.allocate(smallsize)
            byteChannel.read(buffer)
            data = buffer.array()
        }

        detector.setText(data)
        val match = detector.detect()

        return Charset.forName(match.name)
    }

    @Throws(IOException::class)
    private fun scan(workRoot: Path, file: Path, charset: Charset): TaskScanResult {

        val startTime = System.nanoTime()

        val taskMap = HashMap<Int, TaskLine>(10)
        val buffer = ContextBuffer(contextBefore)

        try {
            Files.newBufferedReader(file, guessCharset(file, charset)).use { reader ->
                var line: String? = reader.readLine()
                var lineNumber = 0
                var match = 0

                while (line != null) {
                    // context
                    lineNumber++
                    var taskLine: TaskLine? = null

                    // matching
                    if (criticals.matcher(line).matches()) {
                        taskLine = TaskLine(lineNumber, TaskLevel.CRITICAL, line)
                    } else if (majors.matcher(line).matches()) {
                        taskLine = TaskLine(lineNumber, TaskLevel.MAJOR, line)
                    } else if (minors.matcher(line).matches()) {
                        taskLine = TaskLine(lineNumber, TaskLevel.MINOR, line)
                    }

                    // found match ?
                    if (taskLine != null) {
                        // remember the taskLine
                        taskMap.put(lineNumber, taskLine)

                        // fix context before
                        val reverse = buffer.bufferReversed
                        for (i in 0..contextBefore - 1) {
                            val currentLineNumber = lineNumber - i - 1
                            if (!taskMap.containsKey(currentLineNumber) && reverse[i] != null) {
                                taskMap.put(currentLineNumber, TaskLine(currentLineNumber, TaskLevel.CONTEXT, reverse[i].toString()))
                            }
                        }

                        // prepare context after
                        match = contextAfter
                    } else {
                        // context after match ?
                        if (match > 0) {
                            taskMap.put(lineNumber, TaskLine(lineNumber, TaskLevel.CONTEXT, line))
                            match--
                        }
                    }

                    // remember current line in the buffer
                    buffer.push(line)
                    line = reader.readLine()
                }
            }
        } catch (e: MalformedInputException) {
            return scan(workRoot, file, guessCharset(file, charset))
        }

        val taskLines = ArrayList(taskMap.values)
        Collections.sort(taskLines, taskLineComparator)
        val taskLineArray = taskLines.toTypedArray()
        val estimatedTime = (System.nanoTime() - startTime) / 1000000

        return TaskScanResult(workRoot.relativize(file).toString(), charset.name(), estimatedTime, taskLineArray.toList())
    }

    @Throws(IOException::class)
    fun scan(workRoot: Path, file: Path): TaskScanResult {
        return scan(workRoot, file, StandardCharsets.UTF_8)
    }

    companion object {
        private val taskLineComparator = Comparator<TaskLine> { taskLine1, taskLine2 -> taskLine1.lineNumber - taskLine2.lineNumber }
    }
}