package com.matijasokol.ui_repolist.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import coil.ImageLoader
import com.matijasokol.repo_datasource_test.network.serializeRepoListData
import com.matijasokol.ui_repolist.RepoList
import com.matijasokol.ui_repolist.RepoListState
import com.matijasokol.ui_repolist.coil.FakeImageLoader
import com.matijasokol.ui_repolist.test.TAG_REPO_INFO_MESSAGE
import com.matijasokol.ui_repolist.test.TAG_REPO_LIST_ITEM
import com.matijasokol.ui_repolist.test.TAG_REPO_NAME
import com.matijasokol.ui_repolist.test.TAG_REPO_SEARCH_BAR
import org.junit.Rule
import org.junit.Test

class RepoListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val imageLoader: ImageLoader = FakeImageLoader.build()
    private val repoData = serializeRepoListData(this::class.java.getResource("/repo_list_valid.json").readText())
    private val query = "kotlin"

    @Test
    fun repoListShownCorrectly() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithTag(TAG_REPO_SEARCH_BAR, true).assertTextEquals(query)
        assert(
            composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isNotEmpty()
        )
        composeTestRule.onNodeWithTag(TAG_REPO_INFO_MESSAGE).assertDoesNotExist()
        composeTestRule.onAllNodesWithTag(TAG_REPO_NAME, true).onFirst()
            .assertTextEquals("${repoData.first().author.name}/${repoData.first().name}")
    }
}