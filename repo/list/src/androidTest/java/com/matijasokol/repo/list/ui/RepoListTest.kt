package com.matijasokol.repo.list.ui

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import com.matijasokol.repo.datasourcetest.network.serializeRepoResponseData
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.list.RepoList
import com.matijasokol.repo.list.RepoListState
import com.matijasokol.repo.list.test.TAG_LOADING_INDICATOR
import com.matijasokol.repo.list.test.TAG_REPO_INFO_MESSAGE
import com.matijasokol.repo.list.test.TAG_REPO_LIST_ITEM
import com.matijasokol.repo.list.test.TAG_REPO_NAME
import com.matijasokol.repo.list.test.TAG_REPO_SEARCH_BAR
import org.junit.Rule
import org.junit.Test

class RepoListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val query = "kotlin"

    @Test
    fun repoListSuccessShowData() {
        val state = RepoListState(
            loadState = Paginator.LoadState.Loaded,
            query = query,
            items = serializeRepoResponseData(
                this::class.java.getResource("/repo_list_valid.json").readText(),
            ),
        )

        composeTestRule.setContent {
            RepoList(
                state = state,
                onEvent = {},
            )
        }

        composeTestRule.onNodeWithTag(TAG_REPO_INFO_MESSAGE).assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(TAG_REPO_SEARCH_BAR, true)
            .assertTextEquals(query)

        composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isNotEmpty()
            .let(::assert)

        composeTestRule
            .onAllNodesWithTag(TAG_REPO_NAME, true)
            .onFirst()
            .assertTextEquals("${state.items.first().author.name}/${state.items.first().name}")
    }

    @Test
    fun repoListEmptyShowEmptyScreen() {
        composeTestRule.setContent {
            RepoList(
                state = RepoListState(
                    query = query,
                    items = serializeRepoResponseData(
                        this::class.java.getResource("/repo_list_empty.json").readText(),
                    ),
                ),
                onEvent = {},
            )
        }

        composeTestRule
            .onNodeWithTag(TAG_REPO_SEARCH_BAR, true)
            .assertTextEquals(query)

        composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isEmpty()
            .let(::assert)
    }

    @Test
    fun repoListLoadingShowProgress() {
        composeTestRule.setContent {
            RepoList(
                state = RepoListState(
                    loadState = Paginator.LoadState.Refresh,
                    query = query,
                ),
                onEvent = {},
            )
        }

        composeTestRule
            .onNodeWithTag(TAG_LOADING_INDICATOR)
            .assertExists()
    }
}
