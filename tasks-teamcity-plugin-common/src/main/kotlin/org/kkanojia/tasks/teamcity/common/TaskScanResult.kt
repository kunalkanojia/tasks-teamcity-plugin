package org.kkanojia.tasks.teamcity.common

import org.kkanojia.tasks.teamcity.models.TaskLine
import java.io.Serializable

data class TaskScanResult(val relativePath: String,
                          val charsetName: String,
                          val runTime: Long,
                          val tasks: List<TaskLine>) : Serializable

