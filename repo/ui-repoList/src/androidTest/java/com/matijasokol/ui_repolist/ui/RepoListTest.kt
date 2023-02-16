package com.matijasokol.ui_repolist.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import coil.ImageLoader
import com.matijasokol.repo_datasource_test.network.serializeRepoListData
import com.matijasokol.ui_repolist.RepoList
import com.matijasokol.ui_repolist.RepoListState
import com.matijasokol.ui_repolist.coil.FakeImageLoader
import org.junit.Rule
import org.junit.Test

class RepoListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val imageLoader: ImageLoader = FakeImageLoader.build()

    private val repoData = serializeRepoListData(this::class.java.getResource("/repo_list_valid.json").readText())

    @Test
    fun repoListShownCorrectly() {
        composeTestRule.setContent {
            val state = remember {
                RepoListState(
                    query = "kotlin",
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

        composeTestRule.onNodeWithText(text = "kotlin").assertIsDisplayed()
    }
}