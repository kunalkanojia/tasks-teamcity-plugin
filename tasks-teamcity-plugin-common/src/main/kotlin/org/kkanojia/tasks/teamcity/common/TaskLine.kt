package org.kkanojia.tasks.teamcity.common

import java.io.Serializable

data class TaskLine(val lineNumber: Int, val level: TaskLevel, val line: String) : Serializable
