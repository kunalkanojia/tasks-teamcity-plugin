package org.kkanojia.tasks.teamcity.agent

import jetbrains.buildServer.RunBuildException
import jetbrains.buildServer.agent.BuildFinishedStatus
import jetbrains.buildServer.agent.BuildProcessAdapter
import jetbrains.buildServer.agent.BuildProgressLogger
import jetbrains.buildServer.log.Loggers


abstract class AbstractBuildProcessAdapter protected constructor(protected val progressLogger: BuildProgressLogger) : BuildProcessAdapter() {

    @Volatile private var isFinished: Boolean = false
    @Volatile private var isFailed: Boolean = false
    @Volatile private var isInterrupted: Boolean = false

    init {
        isFinished = false
        isFailed = false
        isInterrupted = false
    }

    override fun interrupt() {
        isInterrupted = true
    }

    override fun isInterrupted(): Boolean {
        return isInterrupted
    }

    override fun isFinished(): Boolean {
        return isFinished
    }

    @Throws(RunBuildException::class)
    override fun waitFor(): BuildFinishedStatus {
        while (!isInterrupted && !isFinished) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                throw RunBuildException(e)
            }

        }

        return if (isFinished){
            if (isFailed) BuildFinishedStatus.FINISHED_FAILED else BuildFinishedStatus.FINISHED_SUCCESS
        } else
            BuildFinishedStatus.INTERRUPTED
    }

    @Throws(RunBuildException::class)
    override fun start() {
        try {
            runProcess()
        } catch (e: RunBuildException) {
            progressLogger.buildFailureDescription(e.message)
            Loggers.AGENT.error(e)
            isFailed = true
        } finally {
            isFinished = true
        }
    }

    @Throws(RunBuildException::class)
    protected abstract fun runProcess()
}