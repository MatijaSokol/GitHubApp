package com.matijasokol.githubapp.backup

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class BackupRulesTest {

    private val legacyRules = rulesOf(parse("backup_rules.xml"))
    private val extractionRules = parse("data_extraction_rules.xml")

    @Test
    fun `legacy full backup rules should NOT BE EMPTY`() {
        legacyRules.shouldNotBeEmpty()
    }

    @Test
    fun `cloud backup rules should MATCH legacy full backup rules`() {
        rulesOf(extractionRules.child("cloud-backup")) shouldBeEqualTo legacyRules
    }

    @Test
    fun `device transfer rules should MATCH legacy full backup rules`() {
        rulesOf(extractionRules.child("device-transfer")) shouldBeEqualTo legacyRules
    }

    private data class Rule(
        val type: String,
        val domain: String,
        val path: String,
    )

    private fun parse(fileName: String): Element = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .parse(File("src/main/res/xml/$fileName"))
        .documentElement

    private fun Element.child(tagName: String): Element = elements().single { it.tagName == tagName }

    private fun rulesOf(section: Element): Set<Rule> = section.elements()
        .filter { it.tagName == "include" || it.tagName == "exclude" }
        .map { Rule(it.tagName, it.getAttribute("domain"), it.getAttribute("path")) }
        .toSet()

    private fun Element.elements(): List<Element> =
        (0 until childNodes.length).map { childNodes.item(it) }.filterIsInstance<Element>()
}
