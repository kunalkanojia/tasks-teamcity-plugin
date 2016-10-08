package org.kkanojia.tasks.teamcity.common


interface StatusLogger {
    abstract fun info(message: String)
}