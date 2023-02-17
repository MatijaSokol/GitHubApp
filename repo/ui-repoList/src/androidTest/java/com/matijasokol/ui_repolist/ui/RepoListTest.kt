package com.matijasokol.ui_repolist.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import coil.ImageLoader
import com.matijasokol.repo_datasource_test.network.serializeRepoListData
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.ui_repolist.RepoList
import com.matijasokol.ui_repolist.RepoListState
import com.matijasokol.ui_repolist.coil.FakeImageLoader
import com.matijasokol.ui_repolist.test.TAG_PULL_TO_REFRESH
import com.matijasokol.ui_repolist.test.TAG_REPO_INFO_MESSAGE
import com.matijasokol.ui_repolist.test.TAG_REPO_LIST_ITEM
import com.matijasokol.ui_repolist.test.TAG_REPO_NAME
import com.matijasokol.ui_repolist.test.TAG_REPO_SEARCH_BAR
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepoListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val imageLoader: ImageLoader = FakeImageLoader.build()
    private val repoData = mutableListOf<Repo>()
    private val query = "kotlin"

    @Before
    fun setUp() {
        repoData.clear()
    }

    @Test
    fun repoListSuccessShowData() {
        composeTestRule.setContent {
            repoData.addAll(serializeRepoListData(this::class.java.getResource("/repo_list_valid.json").readText()))

            val state = remember {
                RepoListState(
                    query = query,
                    items = repoData,
                    removeQueryEnabled = true
                )
            }
            RepoList(
                state = state,
                imageLoader = imageLoader,
                onItemClick = {},
                onImageClick = {},
                onEvent = {}
            )
        }

        composeTestRule.onNodeWithTag(TAG_REPO_INFO_MESSAGE).assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(TAG_REPO_SEARCH_BAR, true)
            .assertTextEquals(query)
        assert(
            composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isNotEmpty()
        )

        composeTestRule
            .onAllNodesWithTag(TAG_REPO_NAME, true)
            .onFirst()
            .assertTextEquals("${repoData.first().author.name}/${repoData.first().name}")
    }

    @Test
    fun repoListEmptyShowEmptyScreen() {
        composeTestRule.setContent {
            repoData.addAll(serializeRepoListData(this::class.java.getResource("/repo_list_empty.json").readText()))

            val state = remember {
                RepoListState(
                    query = query,
                    items = repoData,
                    removeQueryEnabled = true
                )
            }
            RepoList(
                state = state,
                imageLoader = imageLoader,
                onItemClick = {},
                onImageClick = {},
                onEvent = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TAG_REPO_SEARCH_BAR, true)
            .assertTextEquals(query)
        assert(
            composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isEmpty()
        )
    }

    @Test
    fun repoListLoadingShowProgress() {
        composeTestRule.setContent {
            val state = remember {
                RepoListState(
                    isLoading = true,
                    query = query,
                    items = emptyList(),
                    removeQueryEnabled = true
                )
            }
            RepoList(
                state = state,
                imageLoader = imageLoader,
                onItemClick = {},
                onImageClick = {},
                onEvent = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TAG_PULL_TO_REFRESH)
            .assertExists()
    }
}