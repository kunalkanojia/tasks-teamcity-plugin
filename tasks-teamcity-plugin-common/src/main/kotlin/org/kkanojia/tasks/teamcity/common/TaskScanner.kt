package org.kkanojia.tasks.teamcity.common

import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*



class TaskScanner @Throws(IOException::class)
@JvmOverloads constructor(
        private val workingRoot: Path,
        private val reportingRoot: Path,
        private val includes: List<String>,
        private val excludes: List<String>,
        private val minors: List<String>,
        private val majors: List<String>,
        private val criticals: List<String>,
        private val contextBefore: Int = 2,
        private val contextAfter: Int = 5,
        private val failBuild: Boolean = false) {

    @Throws(IOException::class)
    fun Run(interruptionChecker: InterruptionChecker, statusLogger: StatusLogger) {

        // logging the settings
        statusLogger.info(String.format("Root path is [%1\$s].", workingRoot.toString()))
        for (include in includes) {
            statusLogger.info(String.format("Included is [%1\$s].", include))
        }
        for (exclude in excludes) {
            statusLogger.info(String.format("Excluded is [%1\$s].", exclude))
        }

        // gather the files that need to be checked
        statusLogger.info("Start gathering the files to be checked.")
        val visitor = GlobalPatternMatcherFileVisitor(workingRoot, includes, excludes, interruptionChecker)
        Files.walkFileTree(workingRoot, visitor)
        val foundPaths = visitor.foundRelativePaths
        statusLogger.info(String.format("Gathered %1\$d files.", foundPaths.size))

        // execute the scanning
        interruptionChecker.isInterrupted

        // logging the settings
        for (minor in minors) {
            statusLogger.info(String.format("Minors include [%1\$s].", minor))
        }
        for (major in majors) {
            statusLogger.info(String.format("Majors include [%1\$s].", major))
        }
        for (critical in criticals) {
            statusLogger.info(String.format("Criticals include [%1\$s].", critical))
        }

        // create task scanner
        val scanner = TaskPatternScanner(minors, majors, criticals, contextBefore, contextAfter)

        // scan for task patterns
        val scanResults = ArrayList<TaskScanResult>(foundPaths.size)
        for (path in foundPaths) {
            statusLogger.info(String.format("Scanning file [%1\$s]", path.toString()))
            val result = scanner.scan(workingRoot, workingRoot.resolve(path))
            scanResults.add(result)
            interruptionChecker.isInterrupted
        }

        // persist the results somewhere
        try {
            val reportPath = Paths.get(reportingRoot.toString(), TaskBuildRunnerConstants.TASK_REPORTING_FILENAME)
            statusLogger.info(String.format("Storing TaskScanResults in [%1\$s]", reportPath.toString()))
            val fileOut = FileOutputStream(reportPath.toString())
            val objectOut = ObjectOutputStream(fileOut)
            objectOut.writeObject(scanResults)
            objectOut.close()
            val containsCritical = scanResults.map { result -> result.tasks }.flatten().map { list -> list.level }.contains(TaskLevel.CRITICAL)

            if(containsCritical && failBuild){
                throw Exception("Critical tasks found marking the build as failed.")
            }
        } catch (e: IOException) {
            // TODO
        }

    }
}
