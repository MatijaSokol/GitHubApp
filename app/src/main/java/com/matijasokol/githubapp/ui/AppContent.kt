package com.matijasokol.githubapp.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior.Companion
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.matijasokol.coreui.components.LocalSharedTransitionScope
import com.matijasokol.coreui.events.ObserveAsEvent
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.githubapp.navigation.LocalNavigator
import com.matijasokol.githubapp.navigation.LocalNavigatorErrorMapper
import com.matijasokol.githubapp.navigation.NavigationEffect
import com.matijasokol.githubapp.navigation.NavigationErrorMapper
import com.matijasokol.githubapp.navigation.NavigationEvent
import com.matijasokol.githubapp.navigation.Navigator
import com.matijasokol.repo.detail.RepoDetail
import com.matijasokol.repo.detail.RepoDetailAction
import com.matijasokol.repo.detail.RepoDetailViewModel
import com.matijasokol.repo.list.RepoList
import com.matijasokol.repo.list.RepoListAction
import com.matijasokol.repo.list.RepoListAction.NavigateToDetails
import com.matijasokol.repo.list.RepoListViewModel
import kotlinx.coroutines.launch

@Composable
fun AppContent(
    navigator: Navigator,
    navigatorErrorMapper: NavigationErrorMapper,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.statusBars,
    ) { innerPadding ->
        SharedTransitionLayout(modifier = Modifier.padding(innerPadding)) {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this,
                LocalNavigator provides navigator,
                LocalNavigatorErrorMapper provides navigatorErrorMapper,
            ) {
                NavigationContent(sharedTransitionScope = this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun NavigationContent(
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(Destination.RepoList)
    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.current

    NavigationEffect(backStack = backStack)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(
        backNavigationBehavior = BackNavigationBehavior.PopUntilContentChange,
    )

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {
            scope.launch {
                navigator.emitDestination(NavigationEvent.GoBack)
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        sceneStrategies = listOf(listDetailStrategy),
        sharedTransitionScope = sharedTransitionScope,
        transitionSpec = { slideTransition(enterFromLeft = false) },
        popTransitionSpec = { slideTransition(enterFromLeft = true) },
        predictivePopTransitionSpec = { slideTransition(enterFromLeft = true) },
        entryProvider = entryProvider {
            entry<Destination.RepoList>(
                metadata = ListDetailSceneStrategy.listPane(),
            ) {
                RepoListEntry()
            }
            entry<Destination.RepoDetail>(
                metadata = ListDetailSceneStrategy.detailPane(),
            ) { key ->
                RepoDetailEntry(key)
            }
        },
    )
}

@Composable
private fun RepoListEntry() {
    val viewModel: RepoListViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val navigator = LocalNavigator.current
    val navigatorErrorMapper = LocalNavigatorErrorMapper.current

    val lazyStaggeredGridState = rememberLazyStaggeredGridState()

    ObserveAsEvent(viewModel.actions) { action ->
        when (action) {
            is NavigateToDetails -> showDetails(
                navigator,
                navigatorErrorMapper,
                action.authorImageUrl,
                action.repoFullName,
                context,
            )
            is RepoListAction.OpenProfile -> openProfile(action.profileUrl, uriHandler, context)
            RepoListAction.ScrollToTop -> lazyStaggeredGridState.animateScrollToItem(0)
            is RepoListAction.ShowMessage -> Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
        }
    }

    RepoList(
        state = state,
        lazyStaggeredGridState = lazyStaggeredGridState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun RepoDetailEntry(key: Destination.RepoDetail) {
    val viewModel = hiltViewModel<RepoDetailViewModel, RepoDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(key) },
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    ObserveAsEvent(viewModel.actions) { action ->
        when (action) {
            is RepoDetailAction.ShowMessage ->
                Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
        }
    }

    RepoDetail(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

private suspend fun showDetails(
    navigator: Navigator,
    navigatorErrorMapper: NavigationErrorMapper,
    authorImageUrl: String,
    repoFullName: String,
    context: Context,
) {
    navigator.emitDestination(
        NavigationEvent.Destination(route = Destination.RepoDetail(repoFullName, authorImageUrl)),
    ).onLeft {
        Toast.makeText(context, navigatorErrorMapper.map(it), Toast.LENGTH_SHORT).show()
    }
}

fun openProfile(profileUrl: String, uriHandler: UriHandler, context: Context) {
    try {
        uriHandler.openUri(profileUrl)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            context.getString(com.matijasokol.repo.list.R.string.repo_list_message_browser_error),
            Toast.LENGTH_SHORT,
        ).show()
    }
}

private fun slideTransition(
    enterFromLeft: Boolean,
    durationMillis: Int = NAVIGATION_ANIMATION_DURATION_MILLIS,
): ContentTransform {
    val direction = if (enterFromLeft) -1 else 1
    return (
        slideInHorizontally(
            animationSpec = tween(durationMillis = durationMillis),
            initialOffsetX = { fullWidth -> direction * fullWidth },
        ) + fadeIn(animationSpec = tween(durationMillis = durationMillis))
    ) togetherWith (
        slideOutHorizontally(
            animationSpec = tween(durationMillis = durationMillis),
            targetOffsetX = { fullWidth -> -direction * fullWidth },
        ) + fadeOut(animationSpec = tween(durationMillis = durationMillis))
    )
}

private const val NAVIGATION_ANIMATION_DURATION_MILLIS = 300
