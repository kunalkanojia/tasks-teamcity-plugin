package org.kkanojia.tasks.teamcity.common

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.PathMatcher


class RegexPatternMatcherFileVisitor @JvmOverloads constructor(
        root: Path,
        includes: List<String>,
        excludes: List<String>,
        interruptionChecker: InterruptionChecker? = null) : BasePatternMatcherFileVisitor(root, includes, excludes, interruptionChecker) {

    override fun getPathMatcher(pattern: String): PathMatcher {

        return FileSystems.getDefault().getPathMatcher(
                String.format("regex:%1\$s", pattern))
    }
}
