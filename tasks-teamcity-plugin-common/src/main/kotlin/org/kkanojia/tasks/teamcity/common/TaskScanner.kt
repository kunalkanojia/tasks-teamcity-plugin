package org.kkanojia.tasks.teamcity.common

import org.kkanojia.tasks.teamcity.models.TaskLevel
import org.kkanojia.tasks.teamcity.models.TaskLine
import java.io.File
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
        minors.forEach { minor -> statusLogger.info(String.format("Minors include [%1\$s].", minor)) }
        majors.forEach { major -> statusLogger.info(String.format("Majors include [%1\$s].", major)) }
        criticals.forEach { critical -> statusLogger.info(String.format("Criticals include [%1\$s].", critical)) }

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

            val listOfTaskLevels = scanResults.map(TaskScanResult::tasks).flatten().map(TaskLine::level)

            writeStatistics(statusLogger, listOfTaskLevels)

            val containsCritical = listOfTaskLevels.contains(TaskLevel.CRITICAL)

            if (containsCritical && failBuild) {
                throw Exception("Critical tasks found marking the build as failed.")
            }
        } catch (e: IOException) {
        }

    }

    private fun writeStatistics(statusLogger: StatusLogger, listOfTasks: List<TaskLevel>) {

        val criticalCount = listOfTasks.count { level -> level == TaskLevel.CRITICAL }
        val majorCount = listOfTasks.count { level -> level == TaskLevel.MAJOR }
        val minorCount = listOfTasks.count { level -> level == TaskLevel.MINOR }

        val chartPath = Paths.get(reportingRoot.toString(), TaskBuildRunnerConstants.STATISTICS_REPORTING_FILENAME)
        statusLogger.info(String.format("Storing Chart in [%1\$s]", chartPath.toString()))

        val xml = "<?xml version='1.0' encoding='utf-8'?>" +
                "<build>" +
                "<statisticValue key='Pending Critical Tasks' value='${criticalCount}'/>" +
                "<statisticValue key='Pending Major Tasks' value='${majorCount}'/>" +
                "<statisticValue key='Pending Minor Tasks' value='${minorCount}'/>" +
                "</build>"

        File(chartPath.toString()).printWriter().use { out ->
            out.println(xml)
        }

    }
}
