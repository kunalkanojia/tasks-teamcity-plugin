package org.kkanojia.tasks.teamcity.agent

import jetbrains.buildServer.agent.AgentBuildRunnerInfo
import jetbrains.buildServer.agent.BuildAgentConfiguration
import org.kkanojia.tasks.teamcity.common.TaskBuildRunnerConstants


class TaskBuildRunnerInfo : AgentBuildRunnerInfo {

    override fun getType(): String {
        return TaskBuildRunnerConstants.TASK_RUN_TYPE
    }

    override fun canRun(buildAgentConfiguration: BuildAgentConfiguration): Boolean {
        return true
    }
}
