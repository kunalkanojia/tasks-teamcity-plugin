package org.kkanojia.tasks.teamcity.agent

import jetbrains.buildServer.RunBuildException
import jetbrains.buildServer.agent.BuildProgressLogger
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher
import org.kkanojia.tasks.teamcity.common.InterruptionChecker
import org.kkanojia.tasks.teamcity.common.StatusLogger
import org.kkanojia.tasks.teamcity.common.TaskBuildRunnerConstants
import org.kkanojia.tasks.teamcity.common.TaskScanner
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicReference


class TaskBuildProcessAdapter(
        internal val artifactsWatcher: ArtifactsWatcher,
        logger: BuildProgressLogger,
        internal val workingRoot: File,
        internal val reportingRoot: File,
        internal val includes: List<String>,
        internal val excludes: List<String>,
        internal val minors: List<String>,
        internal val majors: List<String>,
        internal val criticals: List<String>,
        internal val failBuild: Boolean) : AbstractBuildProcessAdapter(logger) {

    @Throws(RunBuildException::class)
    override fun runProcess() {
        try {
            // initialize working root path
            val workingRootCanonicalPath = workingRoot.canonicalPath
            progressLogger.message(String.format("The working root path is [%1\$s].", workingRootCanonicalPath))
            val workingRootPath = Paths.get(workingRootCanonicalPath)

            // initialize reporting root path
            val reportingRootCanonicalPath = reportingRoot.canonicalPath
            progressLogger.message(String.format("The reporting root path is [%1\$s].", reportingRootCanonicalPath))
            val reportingRootPath = Paths.get(reportingRootCanonicalPath, TaskBuildRunnerConstants.TASK_REPORTING_FOLDER)
            Files.createDirectory(reportingRootPath)
            progressLogger.message(String.format("Create directory for reporting [%1\$s].", reportingRootPath))

            // initialize the Task Scanner
            val scanner = TaskScanner(
                    workingRootPath,
                    reportingRootPath,
                    includes,
                    excludes,
                    minors,
                    majors,
                    criticals,
                    2,
                    5,
                    failBuild)
            val scannerException = AtomicReference<Exception>(null)
            val interruptibleScanner = Runnable {
                try {
                    scanner.Run(
                            object : InterruptionChecker {
                                override val isInterrupted: Boolean
                                    get() = this@TaskBuildProcessAdapter.isInterrupted
                            },
                            object : StatusLogger {
                                override fun info(message: String) {
                                    progressLogger.message(message)
                                }
                            })
                } catch (e: Exception) {
                    progressLogger.error(e.message)
                    scannerException.set(e)
                }
            }

            // run the Task Scanner
            val scannerThread = Thread(interruptibleScanner)
            scannerThread.start()
            scannerThread.join()

            // register artifacts
            artifactsWatcher.addNewArtifactsPath(reportingRootPath.toString())

            // handle exceptions
            val innerException = scannerException.get()
            if (innerException != null) {
                throw innerException
            }

        } catch (e: RunBuildException) {
            throw e
        } catch (e: Exception) {
            throw RunBuildException(e)
        } finally {

        }

    }
}
