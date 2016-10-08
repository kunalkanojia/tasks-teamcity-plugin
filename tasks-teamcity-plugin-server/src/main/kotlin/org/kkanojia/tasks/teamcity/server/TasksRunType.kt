package org.kkanojia.tasks.teamcity.server

import jetbrains.buildServer.serverSide.PropertiesProcessor
import jetbrains.buildServer.serverSide.RunType
import jetbrains.buildServer.serverSide.RunTypeRegistry
import jetbrains.buildServer.web.openapi.PluginDescriptor
import java.util.*
import org.kkanojia.tasks.teamcity.common.*

class TasksRunType(
        registry: RunTypeRegistry,
        private val pluginDescriptor: PluginDescriptor) : RunType() {

    init {
        registry.registerRunType(this)
    }

    override fun getType(): String {
        return TaskBuildRunnerConstants.TASK_RUN_TYPE
    }

    override fun getDisplayName(): String {
        return "Tasks Build Runner"
    }

    override fun getDescription(): String {
        return "Build Runner that scans sources for marked tasks."
    }

    override fun getRunnerPropertiesProcessor(): PropertiesProcessor? {
        return TasksPropertiesProcessor()
    }

    override fun getEditRunnerParamsJspFilePath(): String? {
        return pluginDescriptor.pluginResourcesPath + "editTaskParams.jsp"
    }

    override fun getViewRunnerParamsJspFilePath(): String? {
        return pluginDescriptor.pluginResourcesPath + "viewTaskParams.jsp"
    }

    override fun getDefaultRunnerProperties(): Map<String, String>? {
        return HashMap()
    }
}