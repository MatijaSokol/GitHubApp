package com.matijasokol.githubapp.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.core.app.ApplicationProvider
import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.coreui.dictionary.DictionaryImpl
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
import com.matijasokol.repo.detail.test.TAG_REPO_DETAIL_SCREEN
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoCache
import com.matijasokol.repo.domain.RepoService
import com.matijasokol.repo.list.test.TAG_REPO_LIST_ITEM
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
        fun provideDictionary(): Dictionary = DictionaryImpl(ApplicationProvider.getApplicationContext())
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var modeChecker: ModeChecker

    @Inject
    lateinit var dictionary: Dictionary

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
        val defaultQuery = "kotlin"
        val query = "android"

        composeTestRule.onNodeWithText(defaultQuery, useUnmergedTree = true).run {
            assertExists()
            performTextReplacement(query)
        }

        composeTestRule.onNodeWithText(query, useUnmergedTree = true).assertExists()
    }

    @Test
    fun testRepoListSorting() {
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM).fetchSemanticsNodes().isNotEmpty()
        }

        // from repo_list_valid.json
        val firstItemText = "JetBrains/kotlin"

        composeTestRule.onNodeWithText(firstItemText, useUnmergedTree = true).assertExists()

        composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM, useUnmergedTree = true)
            .onFirst()
            .onChildren()
            .assertAny(hasText(firstItemText))

        composeTestRule.onNodeWithContentDescription("Sort options").performClick()
        composeTestRule.onNodeWithText(dictionary.getString(com.matijasokol.repo.list.R.string.repo_list_sort_forks_asc)).performClick()

        composeTestRule.onAllNodesWithTag(TAG_REPO_LIST_ITEM, useUnmergedTree = true)
            .onFirst()
            .onChildren()
            .assertAny(!hasText(firstItemText))
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

        composeTestRule.onNodeWithText(firstItemText, true).assertExists()
    }
}
