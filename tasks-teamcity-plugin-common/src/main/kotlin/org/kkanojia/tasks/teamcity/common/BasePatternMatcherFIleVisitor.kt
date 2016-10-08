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
        for (include in includes) {
            includeMatchers.add(getPathMatcher(include))
        }

        // process excludes
        for (exclude in excludes) {
            excludeMatchers.add(getPathMatcher(exclude))
        }
    }// root path
    // interruption checker

    protected abstract fun getPathMatcher(pattern: String): PathMatcher

    private fun isMatchFor(file: Path, matchers: List<PathMatcher>): Boolean {
        for (matcher in matchers) {
            if (matcher.matches(file)) {
                return true
            }
        }
        return false
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

    val foundRelativePaths: List<Path>
        get() = foundPaths

    val foundAbsolutePaths: List<Path>
        get() {
            val result = ArrayList<Path>()
            for (p in foundPaths) {
                result.add(root.resolve(p))
            }
            return result
        }
}

