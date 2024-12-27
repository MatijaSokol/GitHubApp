package com.matijasokol.repo.detail.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.matijasokol.repo.datasourcetest.network.serializeRepoResponseData
import com.matijasokol.repo.detail.RepoDetail
import com.matijasokol.repo.detail.RepoDetailState
import com.matijasokol.repo.detail.RepoUi
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_BUTTON_REPO_WEB
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_ERROR_TEXT
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_INFO_TEXT
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_PROGRESS
import org.junit.Rule
import org.junit.Test

class RepoDetailTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repoData = serializeRepoResponseData(this::class.java.getResource("/repo_list_valid.json").readText())

    @Test
    fun repoDetailShownCorrectly() {
        val repo = repoData.random()
        composeTestRule.setContent {
            val state = remember {
                RepoDetailState.Success(
                    repoFullName = "JetBrains/kotlin",
                    authorImageUrl = "",
                    detailsButtonText = "Details",
                    repoUi = RepoUi(
                        followersCountText = null,
                        reposCountText = null,
                        authorProfileUrl = "",
                        repoUrl = "",
                        topics = emptyList(),
                        info = listOf(
                            "Watchers: ${repo.watchersCount}",
                            "Issues: ${repo.issuesCount}",
                            "Forks: ${repo.forksCount}",
                            "Stars: ${repo.starsCount}",
                        ),
                    ),
                )
            }
            RepoDetail(
                state = state,
                onEvent = {},
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
                RepoDetailState.Error(
                    errorMessage = errorMessageText,
                    repoFullName = "JetBrains/kotlin",
                    authorImageUrl = "",
                )
            }
            RepoDetail(
                state = state,
                onEvent = {},
            )
        }

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_PROGRESS).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_ERROR_TEXT).assertExists()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_ERROR_TEXT).assertTextEquals(errorMessageText)
    }

    @Test
    fun repoDetailLoadingShowsProgress() {
        composeTestRule.setContent {
            val state = remember {
                RepoDetailState.Loading(
                    repoFullName = "JetBrains/kotlin",
                    authorImageUrl = "",
                )
            }
            RepoDetail(
                state = state,
                onEvent = {},
            )
        }

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_PROGRESS).assertExists()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_INFO_TEXT).assertDoesNotExist()
    }
}
