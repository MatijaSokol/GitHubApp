package quality

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class XmlFormatCheckerTest {

  private val sut = XmlFormatChecker()

  @Test
  fun `well formatted file has no violations`() {
    val content = xml(
      "<?xml version=\"1.0\" encoding=\"utf-8\"?>",
      "<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"",
      "    android:width=\"24dp\">",
      "    <group>",
      "        <path",
      "            android:fillColor=\"#FF000000\" />",
      "",
      "    </group>",
      "</vector>",
    )

    sut.check(content).shouldBeEmpty()
  }

  @Test
  fun `empty file has no violations`() {
    sut.check("").shouldBeEmpty()
  }

  @Test
  fun `file containing only a newline has no violations`() {
    sut.check("\n").shouldBeEmpty()
  }

  @Test
  fun `missing newline at end of file is reported on the last line`() {
    val content = "<resources>\n    <string name=\"a\">A</string>\n</resources>"

    sut.check(content) shouldBeEqualTo listOf(
      XmlFormatViolation(3, "Missing newline at end of file"),
    )
  }

  @Test
  fun `whitespace only last line without newline is reported as trailing whitespace and missing newline`() {
    val content = "<resources />\n    "

    sut.check(content) shouldBeEqualTo listOf(
      XmlFormatViolation(2, "Trailing whitespace"),
      XmlFormatViolation(2, "Missing newline at end of file"),
    )
  }

  @Test
  fun `two space indentation is reported`() {
    val content = xml(
      "<vector>",
      "  <group>",
      "    <path />",
      "  </group>",
      "</vector>",
    )

    sut.check(content) shouldBeEqualTo listOf(
      XmlFormatViolation(2, "Indentation width of 2 is not a multiple of 4"),
      XmlFormatViolation(4, "Indentation width of 2 is not a multiple of 4"),
    )
  }

  @Test
  fun `tab indentation counts as one indent level`() {
    val content = xml(
      "<vector>",
      "\t<group>",
      "\t\t<path />",
      "\t    <path />",
      "\t</group>",
      "</vector>",
    )

    sut.check(content).shouldBeEmpty()
  }

  @Test
  fun `tab mixed with spaces is reported when the total width is not a multiple of the indent size`() {
    val content = xml(
      "<resources>",
      "\t  <string name=\"a\">A</string>",
      "</resources>",
    )

    sut.check(content) shouldBeEqualTo listOf(
      XmlFormatViolation(2, "Indentation width of 6 is not a multiple of 4"),
    )
  }

  @Test
  fun `trailing whitespace is reported, including whitespace only lines`() {
    val content = xml(
      "<resources> ",
      "    ",
      "</resources>",
    )

    sut.check(content) shouldBeEqualTo listOf(
      XmlFormatViolation(1, "Trailing whitespace"),
      XmlFormatViolation(2, "Trailing whitespace"),
    )
  }

  @Test
  fun `indentation inside a multi line comment is not checked`() {
    val content = xml(
      "<?xml version=\"1.0\" encoding=\"utf-8\"?><!--",
      "   Sample backup rules file; uncomment and customize as necessary.",
      "-->",
      "<full-backup-content>",
      "    <!--",
      "   <include domain=\"sharedpref\" path=\".\"/>",
      "-->",
      "</full-backup-content>",
    )

    sut.check(content).shouldBeEmpty()
  }

  @Test
  fun `indentation is checked again after a comment closes`() {
    val content = xml(
      "<resources>",
      "    <!-- one line comment -->",
      "  <string name=\"a\">A</string>",
      "    <!--",
      "       free text",
      "    --> <string name=\"b\">B</string>",
      "  <string name=\"c\">C</string>",
      "</resources>",
    )

    sut.check(content) shouldBeEqualTo listOf(
      XmlFormatViolation(3, "Indentation width of 2 is not a multiple of 4"),
      XmlFormatViolation(7, "Indentation width of 2 is not a multiple of 4"),
    )
  }

  @Test
  fun `windows line endings are accepted`() {
    val content = "<resources>\r\n    <string name=\"a\">A</string>\r\n</resources>\r\n"

    sut.check(content).shouldBeEmpty()
  }

  @Test
  fun `custom indent size is used for the indentation check`() {
    val content = xml(
      "<vector>",
      "  <group>",
      "     <path />",
      "  </group>",
      "</vector>",
    )

    val sut = XmlFormatChecker(indentSize = 2)

    sut.check(content) shouldBeEqualTo listOf(
      XmlFormatViolation(3, "Indentation width of 5 is not a multiple of 2"),
    )
  }

  @Test
  fun `non positive indent size is rejected`() {
    assertThrows<IllegalArgumentException> { XmlFormatChecker(indentSize = 0) }
  }

  private fun xml(vararg lines: String) = lines.joinToString(separator = "\n", postfix = "\n")
}
