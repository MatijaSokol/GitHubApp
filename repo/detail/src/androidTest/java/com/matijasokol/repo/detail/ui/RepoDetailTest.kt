package com.matijasokol.repo.detail.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.matijasokol.coreui.components.LocalAnimatedContentScope
import com.matijasokol.coreui.components.LocalSharedTransitionScope
import com.matijasokol.coreui.dictionary.DictionaryImpl
import com.matijasokol.repo.datasourcetest.network.serializeRepoResponseData
import com.matijasokol.repo.detail.R
import com.matijasokol.repo.detail.RepoDetail
import com.matijasokol.repo.detail.RepoDetailState
import com.matijasokol.repo.detail.RepoUi
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_PROGRESS
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class RepoDetailTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repoData = serializeRepoResponseData(this::class.java.getResource("/repo_list_valid.json").readText())
    private val dictionary = DictionaryImpl(ApplicationProvider.getApplicationContext())

    // Workaround to provide required parameters due to shared transition animation
    // Without this, test will fail. See SharedElement.kt for more details
    @SuppressLint("UnusedContentLambdaTargetStateParameter")
    @Composable
    private fun FakeRootComposable(content: @Composable () -> Unit) {
        AnimatedContent(Unit) {
            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this,
                    LocalAnimatedContentScope provides this@AnimatedContent,
                ) {
                    content()
                }
            }
        }
    }

    @Test
    fun repoDetailShownCorrectly() {
        val repo = repoData.random()
        val detailsButtonText = dictionary.getString(R.string.repo_detail_btn_repo_details)
        val infoData = with(dictionary) {
            persistentListOf(
                getString(R.string.repo_detail_panel_watchers, repo.watchersCount),
                getString(R.string.repo_detail_panel_issues, repo.issuesCount),
                getString(R.string.repo_detail_panel_forks, repo.forksCount),
                getString(R.string.repo_detail_panel_stars, repo.starsCount),
            )
        }

        composeTestRule.setContent {
            val state = remember {
                RepoDetailState.Success(
                    repoFullName = "JetBrains/kotlin",
                    authorImageUrl = "",
                    detailsButtonText = detailsButtonText,
                    repoUi = RepoUi(
                        followersCountText = null,
                        reposCountText = null,
                        authorProfileUrl = "",
                        repoUrl = "",
                        topics = persistentListOf(),
                        info = infoData,
                    ),
                )
            }
            FakeRootComposable {
                RepoDetail(
                    state = state,
                    onEvent = {},
                )
            }
        }

        composeTestRule.onNodeWithText(detailsButtonText, useUnmergedTree = true).assertExists()

        infoData.forEach { infoText ->
            composeTestRule.onNodeWithText(infoText, useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun repoDetailErrorShowsErrorMessage() {
        val errorMessageText = dictionary.getString(R.string.repo_detail_message_cache_error)

        composeTestRule.setContent {
            val state = remember {
                RepoDetailState.Error(
                    errorMessage = errorMessageText,
                    repoFullName = "JetBrains/kotlin",
                    authorImageUrl = "",
                )
            }
            FakeRootComposable {
                RepoDetail(
                    state = state,
                    onEvent = {},
                )
            }
        }

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_PROGRESS).assertDoesNotExist()
        composeTestRule.onNodeWithText(errorMessageText, useUnmergedTree = true).assertExists()
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
            FakeRootComposable {
                RepoDetail(
                    state = state,
                    onEvent = {},
                )
            }
        }

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_PROGRESS).assertExists()
    }
}
