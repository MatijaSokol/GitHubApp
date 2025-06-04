package com.matijasokol.githubapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.matijasokol.githubapp.navigation.NavigationErrorMapper
import com.matijasokol.githubapp.navigation.Navigator
import com.matijasokol.githubapp.ui.AppContent
import com.matijasokol.githubapp.ui.theme.GitHubAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigatorErrorMapper: NavigationErrorMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            GitHubAppTheme {
                AppContent(navigator, navigatorErrorMapper)
            }
        }
    }
}
