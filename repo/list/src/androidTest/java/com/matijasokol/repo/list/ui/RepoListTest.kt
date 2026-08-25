package com.matijasokol.repo.list.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.test.core.app.ApplicationProvider
import com.matijasokol.coreui.components.LocalSharedTransitionScope
import com.matijasokol.coreui.dictionary.DictionaryImpl
import com.matijasokol.repo.datasourcetest.network.serializeRepoResponseData
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.list.RepoList
import com.matijasokol.repo.list.RepoListState
import com.matijasokol.repo.list.RepoListUiMapper
import com.matijasokol.repo.list.test.TAG_LOADING_INDICATOR
import com.matijasokol.repo.list.test.TAG_REPO_LIST_ITEM
import kotlinx.collections.immutable.toPersistentList
import org.junit.Rule
import org.junit.Test

class RepoListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val query = "kotlin"
    private val dictionary = DictionaryImpl(ApplicationProvider.getApplicationContext())
    private val uiMapper = RepoListUiMapper(dictionary)
    private val testText = uiMapper.initialState(query).text

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
    fun repoListSuccessShowData() {
        val errorText = testText.loadErrorMessage

        val state = RepoListState(
            loadState = Paginator.LoadState.Loaded,
            query = query,
            items = uiMapper.toUiState(
                loadState = Paginator.LoadState.Loaded,
                items = serializeRepoResponseData(
                    jsonData = this::class.java.getResource("/repo_list_valid.json").readText(),
                ),
                query = query,
                repoSortType = RepoSortType.Unknown(),
            ).items.toPersistentList(),
            text = testText,
        )

        composeTestRule.setContent {
            FakeRootComposable {
                RepoList(
                    state = state,
                    onEvent = {},
                )
            }
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
        val errorText = testText.loadErrorMessage

        val state = RepoListState(
            query = query,
            text = testText,
            items = uiMapper.toUiState(
                loadState = Paginator.LoadState.Loaded,
                items = serializeRepoResponseData(
                    jsonData = this::class.java.getResource("/repo_list_empty.json").readText(),
                ),
                query = query,
                repoSortType = RepoSortType.Unknown(),
            ).items.toPersistentList(),
        )

        composeTestRule.setContent {
            FakeRootComposable {
                RepoList(
                    state = state,
                    onEvent = {},
                )
            }
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
            FakeRootComposable {
                RepoList(
                    state = RepoListState(
                        loadState = Paginator.LoadState.Refresh,
                        query = query,
                        text = testText,
                    ),
                    onEvent = {},
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TAG_LOADING_INDICATOR)
            .assertExists()
    }
}
