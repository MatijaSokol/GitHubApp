package com.matijasokol.repo.detail

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.matijasokol.coreui.text.UiText
import kotlinx.collections.immutable.persistentListOf

internal class RepoDetailStatePreviewParameterProvider : PreviewParameterProvider<RepoDetailState> {

    override val values = RepoDetailPreviewFixtures.states

    override fun getDisplayName(index: Int): String? = when (index) {
        0 -> "Loading"
        1 -> "Success"
        2 -> "Success - Minimal Content"
        3 -> "Success - Long Content"
        4 -> "Error"
        else -> super.getDisplayName(index)
    }
}

internal object RepoDetailPreviewFixtures {
    private val fullInfo = persistentListOf(
        text("Watchers: 49,640"),
        text("Issues: 174"),
        text("Forks: 5,805"),
        text("Stars: 49,640"),
        text("Language: Kotlin"),
        text("Description: The Kotlin Programming Language."),
        text("Updated: Dec 23, 2024"),
    )

    private val loading = RepoDetailState.Loading(
        repoFullName = "JetBrains/kotlin",
        authorImageUrl = "",
        profileSupportingText = text("Maintained by JetBrains"),
    )

    private val error = RepoDetailState.Error(
        errorTitle = text("Repository unavailable"),
        loadErrorMessage = text("We couldn't load this repository's details. Check your connection and try again."),
        retryButtonText = text("Try again"),
        repoFullName = "JetBrains/kotlin",
        authorImageUrl = "",
        profileSupportingText = text("Maintained by JetBrains"),
    )

    val success = RepoDetailState.Success(
        repoUi = RepoUi(
            info = fullInfo,
            followersCountText = text("Followers: 13,143"),
            reposCountText = text("Repositories: 357"),
            authorProfileUrl = "https://github.com/JetBrains",
            repoUrl = "https://github.com/JetBrains/kotlin",
            topics = persistentListOf("compiler", "gradle-plugin", "kotlin", "programming-language"),
        ),
        repositoryLinkTitle = text("View on GitHub"),
        repositoryLinkSubtitle = text("Open repository in your browser"),
        topicsSectionTitle = text("Topics"),
        overviewSectionTitle = text("At a glance"),
        repoFullName = "JetBrains/kotlin",
        authorImageUrl = "",
        profileSupportingText = text("Maintained by JetBrains"),
    )

    val minimalContent = success.copy(
        repoUi = success.repoUi.copy(
            info = persistentListOf(
                text("Watchers: 0"),
                text("Issues: 0"),
                text("Forks: 0"),
                text("Stars: 0"),
                text("Language: "),
                text("Description: "),
                text("Updated: Dec 23, 2024"),
            ),
            followersCountText = null,
            reposCountText = null,
            topics = persistentListOf(),
        ),
        repoFullName = "example/minimal",
        profileSupportingText = text("Maintained by example"),
    )

    val longContent = success.copy(
        repoUi = success.repoUi.copy(
            info = fullInfo.replacingAt(
                5,
                text(
                    "Description: A deliberately long repository description that spans several lines and exposes " +
                        "wrapping, spacing, and increased font-scale layout problems on compact devices.",
                ),
            ),
            topics = persistentListOf(
                "multiplatform-adaptive-navigation",
                "experimental-compose-material-design-components",
            ),
        ),
        repoFullName = "androidx/androidx-compose-material3-adaptive-navigation-suite-experimental",
        profileSupportingText = text("Maintained by an organization with an unusually long display name"),
    )

    val states = sequenceOf(
        loading,
        success,
        minimalContent,
        longContent,
        error,
    )

    private fun text(value: String) = UiText.StringText(value)
}
