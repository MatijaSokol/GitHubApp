package com.matijasokol.githubapp.di

import com.matijasokol.githubapp.ModeChecker
import com.matijasokol.githubapp.navigation.Navigator
import com.matijasokol.githubapp.navigation.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator

    companion object {

        @Provides
        @Singleton
        fun provideModeChecker(): ModeChecker = ModeChecker()
    }
}
