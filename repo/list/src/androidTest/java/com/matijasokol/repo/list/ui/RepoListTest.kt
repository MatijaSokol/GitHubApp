package com.matijasokol.repo.list.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.matijasokol.coreui.dictionary.DictionaryImpl
import com.matijasokol.repo.datasourcetest.network.serializeRepoResponseData
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.model.Repo
import com.matijasokol.repo.list.R
import com.matijasokol.repo.list.RepoList
import com.matijasokol.repo.list.RepoListState
import com.matijasokol.repo.list.test.TAG_LOADING_INDICATOR
import com.matijasokol.repo.list.test.TAG_REPO_LIST_ITEM
import com.matijasokol.repo.list.toRepoListItem
import kotlinx.collections.immutable.toPersistentList
import org.junit.Rule
import org.junit.Test

class RepoListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val query = "kotlin"
    private val dictionary = DictionaryImpl(ApplicationProvider.getApplicationContext())

    @Test
    fun repoListSuccessShowData() {
        val errorText = dictionary.getString(R.string.repo_list_message_error)

        val state = RepoListState(
            loadState = Paginator.LoadState.Loaded,
            query = query,
            items = serializeRepoResponseData(
                this::class.java.getResource("/repo_list_valid.json").readText(),
            ).map(Repo::toRepoListItem).toPersistentList(),
            errorText = errorText,
        )

        composeTestRule.setContent {
            RepoList(
                state = state,
                onEvent = {},
            )
        }

        composeTestRule.onNodeWithText(errorText).assertDoesNotExist()
        composeTestRule
            .onNodeWithText(query, useUnmergedTree = true)
            .assertExists()

        composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isNotEmpty()
            .let(::assert)

        composeTestRule
            .onNodeWithText("${state.items.first().authorName}/${state.items.first().name}")
            .assertExists()
    }

    @Test
    fun repoListEmptyShowErrorMessage() {
        val errorText = dictionary.getString(R.string.repo_list_message_error)

        composeTestRule.setContent {
            RepoList(
                state = RepoListState(
                    query = query,
                    items = serializeRepoResponseData(
                        this::class.java.getResource("/repo_list_empty.json").readText(),
                    ).map(Repo::toRepoListItem).toPersistentList(),
                ),
                onEvent = {},
            )
        }

        composeTestRule
            .onNodeWithText(query, useUnmergedTree = true)
            .assertExists()

        composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isEmpty()
            .let(::assert)

        composeTestRule.onNodeWithText(errorText).assertDoesNotExist()
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
