package com.matijasokol.githubapp.ui

import androidx.activity.compose.setContent
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
import com.matijasokol.coreui.dictionary.Dictionary
import com.matijasokol.coreui.dictionary.FakeDictionary
import com.matijasokol.githubapp.MainActivity
import com.matijasokol.githubapp.ModeChecker
import com.matijasokol.githubapp.di.CacheModule
import com.matijasokol.githubapp.di.CoreModule
import com.matijasokol.githubapp.di.DataSourceModule
import com.matijasokol.githubapp.di.NetworkModule
import com.matijasokol.githubapp.di.ViewModelModule
import com.matijasokol.githubapp.navigation.Navigator
import com.matijasokol.githubapp.ui.theme.GitHubAppTheme
import com.matijasokol.repo.datasourcetest.cache.RepoCacheFake
import com.matijasokol.repo.datasourcetest.cache.RepoDatabaseFake
import com.matijasokol.repo.datasourcetest.network.FakePaginator
import com.matijasokol.repo.datasourcetest.network.RepoServiceFake
import com.matijasokol.repo.datasourcetest.network.RepoServiceResponseType
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_AUTHOR_AND_NAME
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_SCREEN
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoCache
import com.matijasokol.repo.domain.RepoService
import com.matijasokol.repo.list.test.TAG_REPO_LIST_ITEM
import com.matijasokol.repo.list.test.TAG_REPO_NAME
import com.matijasokol.repo.list.test.TAG_REPO_SEARCH_BAR
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
import javax.inject.Inject
import javax.inject.Singleton

// keep NavigationModule in the test module since behavior is same
@UninstallModules(
    CacheModule::class,
    CoreModule::class,
    NetworkModule::class,
    DataSourceModule::class,
    ViewModelModule::class,
)
@HiltAndroidTest
class RepoListEndToEnd {

    @Module
    @InstallIn(SingletonComponent::class)
    object TestModule {

        @Provides
        @Singleton
        fun provideRepoCache(): RepoCache = RepoCacheFake(repoDatabaseFake = RepoDatabaseFake())

        @Provides
        @Singleton
        fun provideRepoService(): RepoService = RepoServiceFake.build(
            type = RepoServiceResponseType.GoodData,
        )

        @Provides
        @Singleton
        fun providePaginator(
            repoService: RepoService,
        ): Paginator = FakePaginator(repoService = repoService)

        @Provides
        @Singleton
        fun provideDictionary(): Dictionary = FakeDictionary()
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var modeChecker: ModeChecker

    @Before
    fun before() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            GitHubAppTheme {
                AppContent(
                    navigator = navigator,
                    modeChecker = modeChecker,
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
                .toString() != firstItemText,
        )
    }

    @Test
    fun testNavigationFromListToDetails() {
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isNotEmpty()
        }

        // from repo_list_valid.json
        val firstItemText = "JetBrains/kotlin"

        composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).onFirst().performClick()
        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_SCREEN).assertExists()

        composeTestRule.onNodeWithTag(TAG_REPO_DETAIL_AUTHOR_AND_NAME, true)
            .assertTextEquals(firstItemText)
    }
}
