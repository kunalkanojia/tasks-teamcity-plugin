package org.kkanojia.tasks.teamcity.server

import jetbrains.buildServer.serverSide.InvalidProperty
import jetbrains.buildServer.serverSide.PropertiesProcessor
import jetbrains.buildServer.util.StringUtil
import org.kkanojia.tasks.teamcity.common.TaskBuildRunnerConstants
import java.util.*

class TasksPropertiesProcessor : PropertiesProcessor {

    override fun process(properties: Map<String, String>): Collection<InvalidProperty> {

        val result = HashSet<InvalidProperty>()

        if (StringUtil.isEmptyOrSpaces(properties[TaskBuildRunnerConstants.PARAM_INCLUDE_REGEX])) {
            result.add(InvalidProperty(TaskBuildRunnerConstants.PARAM_INCLUDE_REGEX, "The parameter 'include regex' must be specified."))
        }
        if (StringUtil.isEmptyOrSpaces(properties[TaskBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX])) {
            result.add(InvalidProperty(TaskBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX, "The parameter 'minor pattern' must be specified."))
        }
        if (StringUtil.isEmptyOrSpaces(properties[TaskBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX])) {
            result.add(InvalidProperty(TaskBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX, "The parameter 'major pattern' must be specified."))
        }
        if (StringUtil.isEmptyOrSpaces(properties[TaskBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX])) {
            result.add(InvalidProperty(TaskBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX, "The parameter 'critical pattern' must be specified."))
        }

        return result
    }
}
