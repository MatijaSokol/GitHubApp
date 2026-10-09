package com.matijasokol.githubapp.navigation

import com.matijasokol.githubapp.ModeChecker
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CanShowDetailsTest {

    private lateinit var sut: CanShowDetails

    @BeforeEach
    fun setUp() {
        sut = CanShowDetails(ModeChecker())
    }

    @Test
    fun `should RETURN FALSE when invoked`() {
        sut() `should be` false
    }
}
