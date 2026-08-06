package com.matijasokol.repo.detail

import com.matijasokol.test.FakeDictionary
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class RepoDetailsUiMapperTest {

    @Test
    fun `should MAP static and formatted text`() {
        val strings = mapOf(
            R.string.repo_detail_message_cache_error to "Could not load details",
            R.string.repo_detail_btn_repo_details to "View on GitHub",
            R.string.repo_detail_btn_repo_details_supporting to "Open in browser",
            R.string.repo_detail_topics_label to "Topics",
            R.string.repo_detail_overview_label to "At a glance",
            R.string.repo_detail_message_profile_browser_error to "Could not open profile",
            R.string.repo_detail_message_repo_browser_error to "Could not open repository",
        )
        val sut = RepoDetailsUiMapper(
            FakeDictionary(
                getString = strings::getValue,
                getStringArgs = { _, args -> "Maintained by ${args.single()}" },
            ),
        )

        val state = sut.loadingState(repoFullName = "JetBrains/kotlin", authorImageUrl = "image")
        val action = sut.toAction(RepoDetailEvent.OpenProfileWebError)

        state.profileSupportingText.shouldBeEqualTo("Maintained by JetBrains")
        action.message.shouldBeEqualTo("Could not open profile")
    }
}
