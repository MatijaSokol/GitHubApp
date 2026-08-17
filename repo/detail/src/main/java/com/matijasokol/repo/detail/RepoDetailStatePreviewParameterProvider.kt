package com.matijasokol.repo.detail

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
        "Watchers: 49,640",
        "Issues: 174",
        "Forks: 5,805",
        "Stars: 49,640",
        "Language: Kotlin",
        "Description: The Kotlin Programming Language.",
        "Updated: Dec 23, 2024",
    )

    private val loading = RepoDetailState.Loading(
        repoFullName = "JetBrains/kotlin",
        authorImageUrl = "",
        profileSupportingText = "Maintained by JetBrains",
    )

    private val error = RepoDetailState.Error(
        errorTitle = "Repository unavailable",
        loadErrorMessage = "We couldn't load this repository's details. Check your connection and try again.",
        retryButtonText = "Try again",
        repoFullName = "JetBrains/kotlin",
        authorImageUrl = "",
        profileSupportingText = "Maintained by JetBrains",
    )

    val success = RepoDetailState.Success(
        repoUi = RepoUi(
            info = fullInfo,
            followersCountText = "Followers: 13,143",
            reposCountText = "Repositories: 357",
            authorProfileUrl = "https://github.com/JetBrains",
            repoUrl = "https://github.com/JetBrains/kotlin",
            topics = persistentListOf("compiler", "gradle-plugin", "kotlin", "programming-language"),
        ),
        repositoryLinkTitle = "View on GitHub",
        repositoryLinkSubtitle = "Open repository in your browser",
        topicsSectionTitle = "Topics",
        overviewSectionTitle = "At a glance",
        repoFullName = "JetBrains/kotlin",
        authorImageUrl = "",
        profileSupportingText = "Maintained by JetBrains",
    )

    val minimalContent = success.copy(
        repoUi = success.repoUi.copy(
            info = persistentListOf(
                "Watchers: 0",
                "Issues: 0",
                "Forks: 0",
                "Stars: 0",
                "Language: ",
                "Description: ",
                "Updated: Dec 23, 2024",
            ),
            followersCountText = null,
            reposCountText = null,
            topics = persistentListOf(),
        ),
        repoFullName = "example/minimal",
        profileSupportingText = "Maintained by example",
    )

    val longContent = success.copy(
        repoUi = success.repoUi.copy(
            info = fullInfo.replacingAt(
                5,
                "Description: A deliberately long repository description that spans several lines and exposes " +
                    "wrapping, spacing, and increased font-scale layout problems on compact devices.",
            ),
            topics = persistentListOf(
                "multiplatform-adaptive-navigation",
                "experimental-compose-material-design-components",
            ),
        ),
        repoFullName = "androidx/androidx-compose-material3-adaptive-navigation-suite-experimental",
        profileSupportingText = "Maintained by an organization with an unusually long display name",
    )

    val states = sequenceOf(
        loading,
        success,
        minimalContent,
        longContent,
        error,
    )
}
