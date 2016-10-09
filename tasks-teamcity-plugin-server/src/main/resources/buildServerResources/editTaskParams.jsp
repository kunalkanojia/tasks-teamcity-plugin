<%@ page import="org.kkanojia.tasks.teamcity.common.TaskBuildRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<l:settingsGroup title="Source filter">
    <tr>
        <th><label for="org.kkanojia.tasks.teamcity.include">Include Patterns:<l:star/></label></th>
        <td><props:multilineProperty name="<%=TaskBuildRunnerConstants.PARAM_INCLUDE_REGEX%>" className="longField"
                                     cols="40" rows="4" expanded="true" linkTitle="Enter include patterns"/>
            <span class="smallNote">Newline separated include patterns, like <kbd>**/*.{java,scala}</kbd>.</span>
            <span class="error" id="error_org.kkanojia.tasks.teamcity.include"></span>
            <span class="smallNoteAttention">A forward slash <kbd>/</kbd> must be used as a directory separator.</span>
        </td>
    </tr>

    <tr>
        <th><label for="org.kkanojia.tasks.teamcity.exclude">Exclude Patterns:</label></th>
        <td><props:multilineProperty name="<%=TaskBuildRunnerConstants.PARAM_EXCLUDE_REGEX%>" className="longField"
                                     cols="40" rows="4" expanded="true" linkTitle="Enter exclude patterns"/>
            <span class="smallNote">Newline separated exclude patterns, like <kbd>dir/**/*.{xml,config}</kbd>."</span>
            <span class="smallNoteAttention">A forward slash <kbd>/</kbd> must be used as a directory separator.</span>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Tasks level filter">
    <tr>
        <th><label for="org.kkanojia.tasks.teamcity.minor">Minor level:<l:star/></label></th>
        <td>
            <props:multilineProperty name="<%=TaskBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX%>"
                                     className="longField" cols="40" rows="4" expanded="true"
                                     linkTitle="Enter minor level regexes"/>
            <span class="smallNote">Newline separated regular expressions for tasks with a minor level.
                Note that the regular expressions are case sensitive and must match the complete line.
                An example is <kbd>.*[Ii][Dd][Ee][Aa].*</kbd>.</span>
            <span class="error" id="error_org.kkanojia.tasks.teamcity.minor"></span>
        </td>
    </tr>
    <tr>
        <th><label for="org.kkanojia.tasks.teamcity.major">Major level:<l:star/></label></th>
        <td>
            <props:multilineProperty name="<%=TaskBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX%>"
                                     className="longField" cols="40" rows="4" expanded="true"
                                     linkTitle="Enter major level regexes"/>
            <span class="smallNote">Newline separated regular expressions for tasks with a major level.
                Note that the regular expressions are case sensitive and must match the complete line.
                An example is <kbd>.*[Tt][Oo][Dd][Oo].*</kbd>.</span>
            <span class="error" id="error_org.kkanojia.tasks.teamcity.major"></span>
        </td>
    </tr>
    <tr>
        <th><label for="org.kkanojia.tasks.teamcity.critical">Critical level:<l:star/></label></th>
        <td>
            <props:multilineProperty name="<%=TaskBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX%>"
                                     className="longField" cols="40" rows="4" expanded="true"
                                     linkTitle="Enter critical level regexes"/>
            <span class="smallNote">Newline separated regular expressions for tasks with a critical level.
                Note that the regular expressions are case sensitive and must match the complete line.
                An example is <kbd>.*[Ff][Ii][Xx][Mm][Ee].*</kbd>.</span>
            <span class="error" id="error_org.kkanojia.tasks.teamcity.critical"></span>
        </td>
    </tr>
    <tr>
        <th><label for="org.kkanojia.tasks.teamcity.build_fail_flag">Fail Build On Critical?:</label></th>
        <td>
            <props:checkboxProperty name="org.kkanojia.tasks.teamcity.build_fail_flag" checked="false" />
        </td>
    </tr>

</l:settingsGroup>
