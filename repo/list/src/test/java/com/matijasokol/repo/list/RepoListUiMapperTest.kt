package com.matijasokol.repo.list

import com.matijasokol.test.FakeDictionary
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class RepoListUiMapperTest {

    @Test
    fun `should MAP static and formatted text to initial state`() {
        val strings = mapOf(
            R.string.repo_list_sort_ascending to "Ascending",
            R.string.repo_list_sort_descending to "Descending",
            R.string.repo_list_title to "Discover",
            R.string.repo_list_subtitle to "Repositories worth exploring",
            R.string.repo_list_query_label to "Search repositories",
            R.string.repo_list_search_content_description to "Search",
            R.string.repo_list_clear_search_content_description to "Clear",
            R.string.repo_list_refresh_error_title to "Could not load repositories",
            R.string.repo_list_message_error to "Loading failed",
            R.string.repo_list_message_browser_error to "Could not open profile",
            R.string.repo_list_retry_text to "Retry",
            R.string.repo_list_sort_options_content_description to "Sort options",
            R.string.repo_list_sort_stars to "Stars",
            R.string.repo_list_sort_forks to "Forks",
            R.string.repo_list_sort_updated to "Updated",
            R.string.repo_list_watchers_content_description to "Watchers",
            R.string.repo_list_forks_content_description to "Forks",
            R.string.repo_list_issues_content_description to "Issues",
        )
        val sut = RepoListUiMapper(
            FakeDictionary(
                getString = strings::getValue,
                getStringArgs = { _, args -> args.joinToString(" ") },
            ),
        )

        val state = sut.initialState(query = "kotlin")

        state.text.headerTitle.shouldBeEqualTo("Discover")
        state.text.searchIconContentDescription.shouldBeEqualTo("Search")
        state.text.profileBrowserErrorMessage.shouldBeEqualTo("Could not open profile")
        state.text.sortOptions.forksOption.displayLabel.shouldBeEqualTo("Forks")
        state.text.sortOptions.forksOption.ascendingActionContentDescription.shouldBeEqualTo("Forks Ascending")
        state.text.sortOptions.forksOption.descendingActionContentDescription.shouldBeEqualTo("Forks Descending")
    }
}
