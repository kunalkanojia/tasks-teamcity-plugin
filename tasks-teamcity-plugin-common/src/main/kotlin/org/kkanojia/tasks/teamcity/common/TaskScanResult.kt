package org.kkanojia.tasks.teamcity.common

import java.io.Serializable

data class TaskScanResult(val relativePath: String,
                          val charsetName: String,
                          val runTime: Long,
                          val tasks: Array<TaskLine>) : Serializable

