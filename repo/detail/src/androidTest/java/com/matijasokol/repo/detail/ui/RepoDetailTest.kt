package com.matijasokol.repo.detail.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.test.core.app.ApplicationProvider
import arrow.core.left
import arrow.core.right
import com.matijasokol.core.error.NetworkError
import com.matijasokol.coreui.components.LocalSharedTransitionScope
import com.matijasokol.coreui.dictionary.DictionaryImpl
import com.matijasokol.repo.datasourcetest.network.serializeRepoResponseData
import com.matijasokol.repo.detail.RepoDetail
import com.matijasokol.repo.detail.RepoDetailState
import com.matijasokol.repo.detail.RepoDetailsUiMapper
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_PROGRESS
import org.junit.Rule
import org.junit.Test

class RepoDetailTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repoData = serializeRepoResponseData(this::class.java.getResource("/repo_list_valid.json").readText())
    private val repoFullName = "JetBrains/kotlin"
    private val dictionary = DictionaryImpl(ApplicationProvider.getApplicationContext())
    private val uiMapper = RepoDetailsUiMapper(dictionary)

    // Workaround to provide required parameters due to shared transition animation
    // Without this, test will fail. See SharedElement.kt for more details
    @SuppressLint("UnusedContentLambdaTargetStateParameter")
    @Composable
    private fun FakeRootComposable(content: @Composable () -> Unit) {
        AnimatedContent(Unit) {
            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this,
                    LocalNavAnimatedContentScope provides this@AnimatedContent,
                ) {
                    content()
                }
            }
        }
    }

    @Test
    fun repoDetailShownCorrectly() {
        val repo = repoData.random()
        val state = uiMapper.toUiState(
            isLoading = false,
            repoOrError = repo.right(),
            repoFullName = repoFullName,
            authorImageUrl = "",
        ) as RepoDetailState.Success

        composeTestRule.setContent {
            FakeRootComposable {
                RepoDetail(
                    state = state,
                    onEvent = {},
                )
            }
        }

        composeTestRule.onNodeWithText(state.repositoryLinkTitle, useUnmergedTree = true).assertExists()

        state.repoUi.info.forEach { infoText ->
            composeTestRule.onNodeWithText(infoText, useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun repoDetailErrorShowsErrorMessage() {
        val state = uiMapper.toUiState(
            isLoading = false,
            repoOrError = NetworkError.UnknownNetworkError.left(),
            repoFullName = repoFullName,
            authorImageUrl = "",
        ) as RepoDetailState.Error

        composeTestRule.setContent {
            FakeRootComposable {
                RepoDetail(
                    state = state,
                    onEvent = {},
                )
            }
        }

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_PROGRESS).assertDoesNotExist()
        composeTestRule.onNodeWithText(state.errorTitle, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText(state.loadErrorMessage, useUnmergedTree = true).assertExists()
    }

    @Test
    fun repoDetailLoadingShowsProgress() {
        val state = uiMapper.loadingState(repoFullName = repoFullName, authorImageUrl = "")

        composeTestRule.setContent {
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
