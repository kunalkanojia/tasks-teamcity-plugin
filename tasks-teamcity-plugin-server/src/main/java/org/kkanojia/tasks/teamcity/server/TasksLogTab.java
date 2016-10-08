package org.kkanojia.tasks.teamcity.server;

import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifacts;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifactsViewMode;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.ViewLogTab;
import org.jetbrains.annotations.NotNull;
import org.kkanojia.tasks.teamcity.common.TaskBuildRunnerConstants;
import org.kkanojia.tasks.teamcity.common.TaskScanResult;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

public class TasksLogTab extends ViewLogTab {

    public TasksLogTab(
            @NotNull final PagePlaces pagePlaces,
            @NotNull final SBuildServer server,
            @NotNull final PluginDescriptor descriptor) {
        super("Task Build Runner", "org.kkanojia.tasks.teamcity.log", pagePlaces, server);
        setIncludeUrl(descriptor.getPluginResourcesPath("todoLogTab.jsp"));
    }

    @Override
    protected void fillModel(Map<String, Object> model, HttpServletRequest request, SBuild build) {
        final BuildArtifacts artifacts = build.getArtifacts(BuildArtifactsViewMode.VIEW_ALL);
        final BuildArtifact artifact = artifacts.getArtifact(TaskBuildRunnerConstants.TASK_REPORTING_FILENAME);
        ArrayList<TaskScanResult> todoScanResults = getTaskScanResults(artifact);
        model.put(TaskBuildRunnerConstants.TASK_REPORTING_IDENTIFICATION, todoScanResults);
    }

    private ArrayList<TaskScanResult> getTaskScanResults(BuildArtifact artifact) {

        ArrayList<TaskScanResult> result = null;

        try {
            InputStream inputStream = artifact.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            result = (ArrayList<TaskScanResult>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException e) {
            // NOP
        } catch (ClassNotFoundException e) {
            // NOP
        }

        return result;
    }
}
