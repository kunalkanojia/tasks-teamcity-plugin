package org.kkanojia.tasks.teamcity.common

object TaskBuildRunnerConstants {
    val TASK_RUN_TYPE = "tasks-build-runner"

    val PARAM_INCLUDE_REGEX = "org.kkanojia.tasks.teamcity.include"
    val PARAM_EXCLUDE_REGEX = "org.kkanojia.tasks.teamcity.exclude"
    val PARAM_PATTERN_MINOR_REGEX = "org.kkanojia.tasks.teamcity.minor"
    val PARAM_PATTERN_MAJOR_REGEX = "org.kkanojia.tasks.teamcity.major"
    val PARAM_PATTERN_CRITICAL_REGEX = "org.kkanojia.tasks.teamcity.critical"

    val TASK_REPORTING_FOLDER = "org.kkanojia.tasks.teamcity.report"

    const val TASK_REPORTING_FILENAME = "TasksScannerResults.ser"

    const val TASK_REPORTING_IDENTIFICATION = "tasksScanResultsReport"
}