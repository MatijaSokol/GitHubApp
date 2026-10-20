package quality

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File

class XmlFormatCheckTaskTest {

  @TempDir
  lateinit var projectDir: File

  private val twoSpaceIndentedXml = "<resources>\n  <string name=\"a\">A</string>\n</resources>\n"

  @Test
  fun `provided checker is used`() {
    val sut = createTask(XmlFormatChecker(indentSize = 2))

    sut.check()

    reportFile().readText() shouldBeEqualTo ""
  }

  @Test
  fun `violations are written to the report and fail the task`() {
    val sut = createTask(XmlFormatChecker(indentSize = 4))

    val exception = assertThrows<GradleException> { sut.check() }

    val message = "Indentation width of 2 is not a multiple of 4"
    reportFile().readText() shouldBeEqualTo "src/main/res/values/strings.xml:2: $message\n"
    exception.message.orEmpty() shouldContain "${xmlFile().toPath().toUri()}:2: $message"
    exception.message.orEmpty() shouldContain "Report: ${reportFile().toPath().toUri()}"
  }

  private fun createTask(checker: XmlFormatChecker): XmlFormatCheckTask {
    val xmlFile = xmlFile()
    xmlFile.parentFile.mkdirs()
    xmlFile.writeText(twoSpaceIndentedXml)

    val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
    return project.tasks.register("xmlFormatCheck", XmlFormatCheckTask::class.java).get().apply {
      sources.from(xmlFile)
      this.checker.set(checker)
      rootDirectory.set(projectDir)
      reportFile.set(reportFile())
    }
  }

  private fun xmlFile() = File(projectDir, "src/main/res/values/strings.xml")

  private fun reportFile() = File(projectDir, "build/reports/xml-format.txt")
}
