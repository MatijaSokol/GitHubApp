package quality

import org.gradle.api.tasks.Input

data class XmlFormatViolation(
  val line: Int,
  val message: String,
)

/**
 * Whitespace rules for XML files: indentation width in multiples of [indentSize], no trailing whitespace and a
 * newline at the end of the file.
 *
 * Indentation may use spaces, tabs or both. A tab counts as [indentSize] spaces, so only the total width is checked:
 * one tab or four spaces pass, while a tab followed by two spaces (width 6) is reported.
 *
 * Lines inside a multi-line `<!-- -->` comment are free text (for example Android Studio template comments), so
 * their indentation is not checked. Trailing whitespace is still reported everywhere.
 *
 * [XmlFormatCheckTask] uses it as a `@Nested` input, so [indentSize] is annotated with `@Input` and changing it
 * invalidates the task's up-to-date state and build cache entry.
 */
class XmlFormatChecker(
  @get:Input val indentSize: Int = DEFAULT_INDENT_SIZE,
) {

  init {
    require(indentSize > 0) { "indentSize must be positive, was $indentSize" }
  }

  fun check(content: String): List<XmlFormatViolation> {
    if (content.isEmpty()) return emptyList()

    val endsWithNewline = content.endsWith('\n')
    val lines = content.split('\n').map { it.removeSuffix("\r") }
      .let { if (endsWithNewline) it.dropLast(1) else it }

    val violations = buildList {
      var inComment = false

      lines.forEachIndexed { index, line ->
        val lineNumber = index + 1

        if (!inComment) {
          indentationViolation(line)?.let { add(XmlFormatViolation(lineNumber, it)) }
        }

        if (line.isNotEmpty() && line.last().isWhitespace()) {
          add(XmlFormatViolation(lineNumber, "Trailing whitespace"))
        }

        inComment = isInCommentAfter(line, inComment)
      }

      if (!endsWithNewline) {
        add(XmlFormatViolation(lines.size, "Missing newline at end of file"))
      }
    }

    return violations
  }

  private fun indentationViolation(line: String): String? {
    val indent = line.takeWhile { it == ' ' || it == '\t' }
    // Whitespace-only lines are reported as trailing whitespace instead.
    if (indent.isEmpty() || indent.length == line.length) return null
    val width = indent.sumOf { if (it == '\t') indentSize else 1 }
    return if (width % indentSize != 0) {
      "Indentation width of $width is not a multiple of $indentSize"
    } else {
      null
    }
  }

  private fun isInCommentAfter(line: String, startsInComment: Boolean): Boolean {
    var inComment = startsInComment
    var index = 0
    while (true) {
      val token = if (inComment) COMMENT_END else COMMENT_START
      val found = line.indexOf(token, index)
      if (found < 0) return inComment
      inComment = !inComment
      index = found + token.length
    }
  }
}

private const val DEFAULT_INDENT_SIZE = 4
private const val COMMENT_START = "<!--"
private const val COMMENT_END = "-->"
