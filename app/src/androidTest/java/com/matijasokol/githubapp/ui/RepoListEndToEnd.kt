package com.matijasokol.githubapp.ui

import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matijasokol.githubapp.MainActivity
import com.matijasokol.githubapp.ModeChecker
import com.matijasokol.githubapp.R
import com.matijasokol.githubapp.coil.FakeImageLoader
import com.matijasokol.githubapp.di.CacheModule
import com.matijasokol.githubapp.di.DataSourceModule
import com.matijasokol.githubapp.di.NetworkModule
import com.matijasokol.githubapp.di.ViewModelModule
import com.matijasokol.githubapp.navigation.Screen
import com.matijasokol.githubapp.ui.theme.GitHubAppTheme
import com.matijasokol.repo_datasource_test.cache.RepoCacheFake
import com.matijasokol.repo_datasource_test.cache.RepoDatabaseFake
import com.matijasokol.repo_datasource_test.network.FakePaginator
import com.matijasokol.repo_datasource_test.network.RepoServiceFake
import com.matijasokol.repo_datasource_test.network.RepoServiceResponseType
import com.matijasokol.repo_domain.Paginator
import com.matijasokol.repo_domain.RepoCache
import com.matijasokol.repo_domain.RepoService
import com.matijasokol.ui_repodetail.RepoDetail
import com.matijasokol.ui_repodetail.RepoDetailViewModel
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_FOLLOWERS_COUNT
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_REPOS_COUNT
import com.matijasokol.ui_repodetail.test.TAG_REPO_DETAIL_SCREEN
import com.matijasokol.ui_repolist.RepoList
import com.matijasokol.ui_repolist.RepoListViewModel
import com.matijasokol.ui_repolist.test.TAG_REPO_LIST_ITEM
import com.matijasokol.ui_repolist.test.TAG_REPO_NAME
import com.matijasokol.ui_repolist.test.TAG_REPO_SEARCH_BAR
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@UninstallModules(
    CacheModule::class,
    NetworkModule::class,
    DataSourceModule::class,
    ViewModelModule::class
)
@HiltAndroidTest
class RepoListEndToEnd {

    @Module
    @InstallIn(SingletonComponent::class)
    object TestRepoUseCasesModule {

        @Provides
        @Singleton
        fun provideRepoCache(): RepoCache {
            return RepoCacheFake(
                repoDatabaseFake = RepoDatabaseFake()
            )
        }

        @Provides
        @Singleton
        fun provideRepoService(): RepoService {
            return RepoServiceFake.build(
                type = RepoServiceResponseType.GoodData
            )
        }

        @Provides
        @Singleton
        fun providePaginator(
            repoService: RepoService,
            repoCache: RepoCache
        ): Paginator {
            return FakePaginator(
                repoService = repoService,
                repoCache = repoCache
            )
        }
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val imageLoader = FakeImageLoader.build()

    @Before
    fun before() {
        composeTestRule.activity.setContent {
            GitHubAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.RepoList.route,
                    builder = {
                        composable(
                            route = Screen.RepoList.route
                        ) {
                            val viewModel: RepoListViewModel = hiltViewModel()
                            val state = viewModel.state.collectAsState()

                            val context = LocalContext.current
                            val uriHandler = LocalUriHandler.current

                            RepoList(
                                state = state.value,
                                onEvent = viewModel::onEvent,
                                onItemClick = { repo ->
                                    if (ModeChecker.CAN_NAVIGATE_TO_DETAILS) {
                                        navController.navigate(
                                            route = "${Screen.RepoDetail.route}/${repo.id}"
                                        )
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.mode_checker_navigation_disabled_message), Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onImageClick = {
                                    try {
                                        uriHandler.openUri(it.profileUrl)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, context.getString(com.matijasokol.ui_repolist.R.string.repo_list_message_browser_error), Toast.LENGTH_SHORT).show()
                                    }
                                },
                                imageLoader = imageLoader
                            )
                        }
                        composable(
                            route = Screen.RepoDetail.route + "/{repoId}",
                            arguments = Screen.RepoDetail.arguments
                        ) {
                            val viewModel: RepoDetailViewModel = hiltViewModel()
                            val state = viewModel.state.collectAsState()
                            RepoDetail(
                                state = state.value,
                                imageLoader = imageLoader
                            )
                        }
                    }
                )
            }
        }
    }

    @Test
    fun testRepoListSearchBar() {
        val query = "android"
        composeTestRule.onNodeWithTag(TAG_REPO_SEARCH_BAR).performTextClearance()
        composeTestRule.onNodeWithTag(TAG_REPO_SEARCH_BAR).performTextInput(query)
        composeTestRule.onNodeWithTag(TAG_REPO_SEARCH_BAR, true).assertTextEquals(query)
    }

    @Test
    fun testRepoListSorting() {
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isNotEmpty()
        }

        // from repo_list_valid.json
        val firstItemText = "JetBrains/kotlin"

        composeTestRule.onAllNodesWithTag(TAG_REPO_NAME, true).onFirst().assertTextEquals(firstItemText)

        composeTestRule.onNodeWithContentDescription("Sort options").performClick()
        composeTestRule.onNodeWithText("Forks ASC").performClick()

        assert(
            composeTestRule.onAllNodesWithTag(TAG_REPO_NAME, true)
                .onFirst().fetchSemanticsNode()
                .config[SemanticsProperties.Text]
                .toString() != firstItemText
        )
    }

    @Test
    fun testNavigationFromListToDetails() {
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).onFirst().performClick()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_SCREEN).assertExists()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_FOLLOWERS_COUNT, true)
            .assertTextEquals(composeTestRule.activity.getString(com.matijasokol.ui_repodetail.R.string.repo_detail_followers_count_text, 30))
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_REPOS_COUNT, true)
            .assertTextEquals(composeTestRule.activity.getString(com.matijasokol.ui_repodetail.R.string.repo_detail_repos_count_text, 30))
    }
}