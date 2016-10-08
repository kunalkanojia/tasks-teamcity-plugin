package org.kkanojia.tasks.teamcity.server

import jetbrains.buildServer.serverSide.SBuild
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact
import jetbrains.buildServer.serverSide.artifacts.BuildArtifactsViewMode
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.ViewLogTab
import org.kkanojia.tasks.teamcity.common.TaskBuildRunnerConstants
import org.kkanojia.tasks.teamcity.common.TaskScanResult
import java.io.IOException
import java.io.ObjectInputStream
import java.util.*
import javax.servlet.http.HttpServletRequest

class LogTab(
        pagePlaces: PagePlaces,
        server: SBuildServer,
        descriptor: PluginDescriptor) : ViewLogTab("Tasks Build Runner", "org.kkanojia.tasks.teamcity.log", pagePlaces, server) {

    init {
        includeUrl = descriptor.getPluginResourcesPath("taskLogTab.jsp")
    }

    override fun fillModel(model: MutableMap<String, Any>, request: HttpServletRequest, build: SBuild) {
        val artifacts = build.getArtifacts(BuildArtifactsViewMode.VIEW_ALL)
        val artifact = artifacts.getArtifact(TaskBuildRunnerConstants.TASK_REPORTING_FILENAME)
        val taskScanResults = getTasksScanResults(artifact!!)
        model.put(TaskBuildRunnerConstants.TASK_REPORTING_IDENTIFICATION, taskScanResults)
    }

    private fun getTasksScanResults(artifact: BuildArtifact): ArrayList<TaskScanResult> {
        var result: ArrayList<TaskScanResult>? = null

        try {
            val inputStream = artifact.inputStream
            val objectInputStream = ObjectInputStream(inputStream)
            result = objectInputStream.readObject() as ArrayList<TaskScanResult>
            objectInputStream.close()
        } catch (e: IOException) {
            // NOP
        } catch (e: ClassNotFoundException) {
            // NOP
        }

        return result!!
    }
}