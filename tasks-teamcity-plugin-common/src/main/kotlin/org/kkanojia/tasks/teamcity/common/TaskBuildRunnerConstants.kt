package org.kkanojia.tasks.teamcity.common

object TaskBuildRunnerConstants {
    val TASK_RUN_TYPE = "tasks-build-runner"

    const val PARAM_INCLUDE_REGEX = "org.kkanojia.tasks.teamcity.include"
    const val PARAM_EXCLUDE_REGEX = "org.kkanojia.tasks.teamcity.exclude"
    const val PARAM_PATTERN_MINOR_REGEX = "org.kkanojia.tasks.teamcity.minor"
    const val PARAM_PATTERN_MAJOR_REGEX = "org.kkanojia.tasks.teamcity.major"
    const val PARAM_PATTERN_CRITICAL_REGEX = "org.kkanojia.tasks.teamcity.critical"

    const val TASK_REPORTING_FOLDER = "org.kkanojia.tasks.teamcity.report"

    const val TASK_REPORTING_FILENAME = "TasksScannerResults.ser"

    const val TASK_REPORTING_IDENTIFICATION = "tasksScanResultsReport"
}