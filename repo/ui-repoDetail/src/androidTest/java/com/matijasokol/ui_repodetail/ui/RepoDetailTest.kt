package com.matijasokol.ui_repodetail.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import coil.ImageLoader
import com.matijasokol.repo_datasource_test.network.serializeRepoListData
import com.matijasokol.ui_repodetail.RepoDetail
import com.matijasokol.ui_repodetail.RepoDetailState
import com.matijasokol.ui_repodetail.coil.FakeImageLoader
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_BUTTON_REPO_WEB
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_ERROR_TEXT
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_INFO_TEXT
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_PROGRESS
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_SCREEN
import org.junit.Rule
import org.junit.Test

class RepoDetailTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val imageLoader: ImageLoader = FakeImageLoader.build()
    private val repoData = serializeRepoListData(this::class.java.getResource("/repo_list_valid.json").readText())

    @Test
    fun repoDetailShownCorrectly() {
        val repo = repoData.random()
        composeTestRule.setContent {
            val state = remember {
                RepoDetailState(
                    repo = repo,
                    errorMessage = null,
                    isLoading = false
                )
            }
            RepoDetail(
                state = state,
                imageLoader = imageLoader
            )
        }

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_BUTTON_REPO_WEB).assertExists()
        composeTestRule.onAllNodesWithTag(TAG_REPO_DETAIL_INFO_TEXT)
            .assertAny(hasText(repo.watchersCount.toString(), true))
    }

    @Test
    fun repoDetailErrorShowsErrorMessage() {
        val errorMessageText = "Error message"
        composeTestRule.setContent {
            val state = remember {
                RepoDetailState(
                    repo = null,
                    errorMessage = errorMessageText,
                    isLoading = false
                )
            }
            RepoDetail(
                state = state,
                imageLoader = imageLoader
            )
        }

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_PROGRESS).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_SCREEN).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_ERROR_TEXT).assertExists()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_ERROR_TEXT).assertTextEquals(errorMessageText)
    }

    @Test
    fun repoDetailLoadingShowsProgress() {
        composeTestRule.setContent {
            val state = remember {
                RepoDetailState(
                    repo = null,
                    errorMessage = null,
                    isLoading = true
                )
            }
            RepoDetail(
                state = state,
                imageLoader = imageLoader
            )
        }

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_PROGRESS).assertExists()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_SCREEN).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_INFO_TEXT).assertDoesNotExist()
    }
}