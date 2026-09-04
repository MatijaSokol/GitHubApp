package com.matijasokol.repo.list

import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Author
import com.matijasokol.repo.domain.model.Repo
import com.matijasokol.test.FakeDictionary
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.util.Date

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
            R.string.repo_list_empty_result_title to "No repositories found",
            R.string.repo_list_empty_result_message to "Try another search term",
            R.string.repo_list_message_error to "Loading failed",
            R.string.repo_list_message_browser_error to "Could not open profile",
            R.string.repo_list_retry_text to "Retry",
            R.string.repo_list_sort_options_content_description to "Sort options",
            R.string.repo_list_sort_stars to "Stars",
            R.string.repo_list_sort_forks to "Forks",
            R.string.repo_list_sort_updated to "Updated",
            R.string.repo_list_stars_content_description to "Stars",
            R.string.repo_list_watchers_content_description to "Watchers",
            R.string.repo_list_forks_content_description to "Forks",
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
        state.text.emptyResultTitle.shouldBeEqualTo("No repositories found")
        state.text.emptyResultMessage.shouldBeEqualTo("Try another search term")
        state.text.profileBrowserErrorMessage.shouldBeEqualTo("Could not open profile")
        state.text.sortOptions.forksOption.displayLabel.shouldBeEqualTo("Forks")
        state.text.sortOptions.forksOption.ascendingActionContentDescription.shouldBeEqualTo("Forks Ascending")
        state.text.sortOptions.forksOption.descendingActionContentDescription.shouldBeEqualTo("Forks Descending")
    }

    @Test
    fun `should MAP compact repository metrics and full accessibility counts`() {
        val metricLabels = mapOf(
            R.string.repo_list_stars_content_description to "Stars",
            R.string.repo_list_watchers_content_description to "Watchers",
            R.string.repo_list_forks_content_description to "Forks",
        )
        val sut = RepoListUiMapper(
            FakeDictionary(
                getStringArgs = { id, args ->
                    metricLabels[id]?.let { "$it: ${args.single()}" } ?: args.joinToString(" ")
                },
            ),
        )
        val repo = Repo(
            id = 1,
            name = "repo",
            fullName = "owner/repo",
            author = Author(1, "owner", "image", "profile", "followers", "repos"),
            watchersCount = 999,
            forksCount = 1_000_000,
            issuesCount = 10,
            lastUpdated = Date(0),
            starsCount = 1_200,
            topics = emptyList(),
            language = null,
            url = "url",
            description = null,
        )

        val item = sut.toUiState(
            loadState = Paginator.LoadState.Loaded,
            items = listOf(repo),
            query = "kotlin",
            repoSortType = RepoSortType.Unknown(),
        ).items.single()

        item.stars shouldBeEqualTo "1.2k"
        item.starsContentDescription shouldBeEqualTo "Stars: 1200"
        item.watchers shouldBeEqualTo "999"
        item.watchersContentDescription shouldBeEqualTo "Watchers: 999"
        item.forks shouldBeEqualTo "1m"
        item.forksContentDescription shouldBeEqualTo "Forks: 1000000"
    }

    @Test
    fun `should FORMAT representative compact count boundaries`() {
        mapOf(
            0 to "0",
            999 to "999",
            1_000 to "1k",
            1_200 to "1.2k",
            999_949 to "999.9k",
            999_950 to "1m",
            1_000_000 to "1m",
            1_250_000 to "1.3m",
            Int.MAX_VALUE to "2.1b",
        ).forEach { (count, expected) ->
            formatCompactCount(count) shouldBeEqualTo expected
        }
    }
}
