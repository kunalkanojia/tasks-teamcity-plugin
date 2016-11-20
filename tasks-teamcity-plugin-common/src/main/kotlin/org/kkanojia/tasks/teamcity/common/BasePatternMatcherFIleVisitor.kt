package org.kkanojia.tasks.teamcity.common

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.PathMatcher
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

abstract class BasePatternMatcherFileVisitor @JvmOverloads constructor(
        private val root: Path,
        includes: List<String>,
        excludes: List<String>,
        private val interruptionChecker: InterruptionChecker? = null) : SimpleFileVisitor<Path>() {

    private val includeMatchers = ArrayList<PathMatcher>()
    private val excludeMatchers = ArrayList<PathMatcher>()
    private val foundPaths = ArrayList<Path>()

    init {

        // process includes
        includes.mapTo(includeMatchers) { getPathMatcher(it) }

        // process excludes
        excludes.mapTo(excludeMatchers) { getPathMatcher(it) }
    }

    protected abstract fun getPathMatcher(pattern: String): PathMatcher

    private fun isMatchFor(file: Path, matchers: List<PathMatcher>): Boolean {
        return matchers.find { it.matches(file) } != null
    }

    private fun isIncluded(file: Path): Boolean {
        return isMatchFor(file, includeMatchers)
    }

    private fun isExcluded(file: Path): Boolean {
        return isMatchFor(file, excludeMatchers)
    }

    @Throws(IOException::class)
    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        if (interruptionChecker != null && interruptionChecker.isInterrupted) {
            return FileVisitResult.TERMINATE
        }

        val relativeFile = root.relativize(file)
        if (isIncluded(relativeFile) && !isExcluded(relativeFile)) {
            foundPaths.add(relativeFile)
        }

        return super.visitFile(file, attrs)
    }

    val foundRelativePaths = foundPaths

}

