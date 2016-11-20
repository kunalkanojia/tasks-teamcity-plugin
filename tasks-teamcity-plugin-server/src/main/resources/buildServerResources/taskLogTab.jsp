<%@ page import="org.kkanojia.tasks.teamcity.models.TaskLevel" %>
<%@ page import="org.kkanojia.tasks.teamcity.models.TaskLine" %>
<%@ page import="org.kkanojia.tasks.teamcity.common.TaskScanResult" %>
<%@ page import="org.unbescape.html.HtmlEscape" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="tasksScanResultsReport" scope="request"
             type="java.util.ArrayList<org.kkanojia.tasks.teamcity.common.TaskScanResult>"/>

<div>
    <script type="text/javascript">
        var taskContextVisibility = 0;

        function taskContextVisible() {
            var i;
            var elements = document.getElementsByClassName('taskline-context');
            for (i = 0; i < elements.length; i++) {
                elements[i].style.display = 'table-row'
            }
            taskContextVisibility = 1;
        }

        function taskContextInvisible() {
            var i;
            var elements = document.getElementsByClassName('taskline-context');
            for (i = 0; i < elements.length; i++) {
                elements[i].style.display = 'none'
            }
            taskContextVisibility = 0;
        }

        function toggleTaskContextVisibility() {
            if (taskContextVisibility == 0) {
                taskContextVisible();
            } else {
                taskContextInvisible();
            }
        }

        function toggleTaskContextVisibilityForFile(fileIndex) {
            var i;
            var elements = document.getElementsByClassName("taskline-context-" + fileIndex);
            if (elements.length > 0) {
                var invisible = elements[0].style.display == 'none';
                var newsetting = invisible ? 'table-row' : 'none';
                for (i = 0; i < elements.length; i++) {
                    elements[i].style.display = newsetting;
                }
            }
        }
    </script>
    <style>
        .color-box {
            width: 10px;
            height: 10px;
            display: inline-block;
            position: absolute;
            border: 1px solid gray;
        }
        .button {
            display: block;
            width: 115px;
            height: 25px;
            background: #62a3af;
            padding: 5px;
            text-align: center;
            color: white;
            cursor: pointer;
        }
    </style>

    <p>
        <a class="button" onclick="toggleTaskContextVisibility()">Toggle context</a>
    </p>

    <table>
        <thead>
        <tr style="background-color: ghostwhite">
            <th colspan="3" style="text-align: center">File name</th>
        </tr>
        </thead>
        <tbody>
        <% int fileIndex = 0; %>
        <% for (TaskScanResult taskScanResult : tasksScanResultsReport) { %>
        <% fileIndex++; %>
        <% if (taskScanResult.getTasks().size() > 0) { %>
        <tr style="background-color: lightgrey; cursor:pointer;">
            <th colspan="3"
                onclick="toggleTaskContextVisibilityForFile(<%=fileIndex%>)"><%= taskScanResult.getRelativePath() %>
            </th>
        </tr>
        <tr style="background-color: ghostwhite">
            <th>Line</th>
            <th>Task</th>
            <th>Level</th>
        </tr>
        <% for (TaskLine taskLine : taskScanResult.getTasks()) { %>
        <% if (taskLine.getLevel() == TaskLevel.CONTEXT) { %>
        <tr class="taskline-context taskline-context-<%=fileIndex%>" style="display: none; padding: 5px;">
            <td style="text-align: right; color: darkgrey"><%= taskLine.getLineNumber() %>
            </td>
            <td style="text-align: left; color: darkgrey; font-family: monospace; padding-left: 10px;"><%= HtmlEscape.escapeHtml4(taskLine.getLine()) %>
            </td>
            <td></td>
        </tr>
        <% } else { %>
        <tr style="padding: 5px;">
            <td style="text-align: right"><%= taskLine.getLineNumber() %>
            </td>
            <td style="text-align: left; font-family: monospace; padding-left: 10px;"><%= HtmlEscape.escapeHtml4(taskLine.getLine()) %>
            </td>
            <td style="text-align: center">
                <% switch (taskLine.getLevel()) {
                    case MINOR: %>
                <div title="MINOR" class="color-box" style="background-color: #88d9ff;"></div>
                <% break;
                    case MAJOR: %>
                <div title="MAJOR" class="color-box" style="background-color: #f9ff5c;"></div>
                <% break;
                    case CRITICAL: %>
                <div title="CRITICAL" class="color-box" style="background-color: #ff6b6b;"></div>
                <% break;
                }
                } %>
            </td>
        </tr>
        <% } %>
        <% } %>
        <% } %>
        </tbody>
    </table>

</div>

