package com.matijasokol.githubapp.navigation

import com.matijasokol.githubapp.ModeChecker
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CanShowDetailsTest {

    private lateinit var sut: CanShowDetailsUseCase

    @BeforeEach
    fun setUp() {
        sut = CanShowDetailsUseCase(ModeChecker())
    }

    @Test
    fun `should RETURN FALSE when use case is called`() {
        sut() `should be` false
    }
}
