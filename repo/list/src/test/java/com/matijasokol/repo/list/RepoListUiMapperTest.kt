package com.matijasokol.repo.list

import com.matijasokol.coreui.text.UiText
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Author
import com.matijasokol.repo.domain.model.Repo
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.util.Date

class RepoListUiMapperTest {

    private val sut = RepoListUiMapper()

    @Test
    fun `should MAP static and formatted text to initial state`() {
        val state = sut.initialState(query = "kotlin")

        state.text.headerTitle.shouldBeEqualTo(UiText.StringResource(R.string.repo_list_title))
        state.text.searchIconContentDescription.shouldBeEqualTo(
            UiText.StringResource(R.string.repo_list_search_content_description),
        )
        state.text.emptyResultTitle.shouldBeEqualTo(UiText.StringResource(R.string.repo_list_empty_result_title))
        state.text.emptyResultMessage.shouldBeEqualTo(UiText.StringResource(R.string.repo_list_empty_result_message))
        sut.profileBrowserErrorMessage().shouldBeEqualTo(
            UiText.StringResource(R.string.repo_list_message_browser_error),
        )
        val forks = UiText.StringResource(R.string.repo_list_sort_forks)
        state.text.sortOptions.forksOption.displayLabel.shouldBeEqualTo(forks)
        state.text.sortOptions.forksOption.ascendingActionContentDescription.shouldBeEqualTo(
            UiText.StringResource(
                R.string.repo_list_sort_direction_content_description,
                forks,
                UiText.StringResource(R.string.repo_list_sort_ascending),
            ),
        )
        state.text.sortOptions.forksOption.descendingActionContentDescription.shouldBeEqualTo(
            UiText.StringResource(
                R.string.repo_list_sort_direction_content_description,
                forks,
                UiText.StringResource(R.string.repo_list_sort_descending),
            ),
        )
    }

    @Test
    fun `should MAP compact repository metrics and full accessibility counts`() {
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
        item.starsContentDescription shouldBeEqualTo
            UiText.StringResource(R.string.repo_list_stars_content_description, 1_200)
        item.watchers shouldBeEqualTo "999"
        item.watchersContentDescription shouldBeEqualTo
            UiText.StringResource(R.string.repo_list_watchers_content_description, 999)
        item.forks shouldBeEqualTo "1m"
        item.forksContentDescription shouldBeEqualTo
            UiText.StringResource(R.string.repo_list_forks_content_description, 1_000_000)
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
