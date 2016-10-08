package org.kkanojia.tasks.teamcity.common

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.PathMatcher

class MultiPatternMatcherFileVisitor @JvmOverloads constructor(
        root: Path,
        includes: List<String>,
        excludes: List<String>,
        interruptionChecker: InterruptionChecker? = null) : BasePatternMatcherFileVisitor(root, includes, excludes, interruptionChecker) {

    override fun getPathMatcher(typeAndPattern: String): PathMatcher {

        val split = typeAndPattern.indexOf(':')
        val type = typeAndPattern.substring(0, split - 1)
        val pattern = typeAndPattern.substring(split + 1)

        return FileSystems.getDefault().getPathMatcher(String.format("%1\$s:%2\$s", type, pattern))
    }
}
