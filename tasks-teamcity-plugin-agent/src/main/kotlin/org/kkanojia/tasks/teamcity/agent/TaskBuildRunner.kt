package org.kkanojia.tasks.teamcity.agent

import jetbrains.buildServer.RunBuildException
import jetbrains.buildServer.agent.*
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher
import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.util.StringUtil
import org.kkanojia.tasks.teamcity.common.TaskBuildRunnerConstants
import java.util.*

class TaskBuildRunner(private val artifactsWatcher: ArtifactsWatcher) : AgentBuildRunner {

    init {
        Loggers.AGENT.warn("TaskBuildRunner created.")
    }

    private fun getValuesFor(parameters: Map<String, String>, parameter: String): List<String> {
        val value = parameters[parameter]
        if (value == null) {
            return ArrayList()
        } else {
            return StringUtil.split(value, true, '\r', '\n')
        }
    }

    @Throws(RunBuildException::class)
    override fun createBuildProcess(build: AgentRunningBuild,
                                    context: BuildRunnerContext): BuildProcess {

        Loggers.AGENT.warn("TaskBuildRunner: createBuildProcess.")

        val runnerParameters = context.runnerParameters

        val includes = getValuesFor(runnerParameters, TaskBuildRunnerConstants.PARAM_INCLUDE_REGEX)
        val excludes = getValuesFor(runnerParameters, TaskBuildRunnerConstants.PARAM_EXCLUDE_REGEX)

        val minors = getValuesFor(runnerParameters, TaskBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX)
        val majors = getValuesFor(runnerParameters, TaskBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX)
        val criticals = getValuesFor(runnerParameters, TaskBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX)
        val failBuild: String = runnerParameters.getOrElse(TaskBuildRunnerConstants.PARAM_FAIL_BUILD_FLAG) { "false" }

        val workingRoot = build.checkoutDirectory
        val reportingRoot = build.buildTempDirectory

        return TaskBuildProcessAdapter(
                artifactsWatcher,
                build.buildLogger,
                workingRoot,
                reportingRoot,
                includes,
                excludes,
                minors,
                majors,
                criticals,
                failBuild.toBoolean())
    }

    override fun getRunnerInfo(): AgentBuildRunnerInfo {
        return TaskBuildRunnerInfo()
    }
}