package com.matijasokol.githubapp.di

import com.matijasokol.coreui.dictionary.Dictionary
import com.matijasokol.coreui.dictionary.DictionaryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindDictionary(dictionaryImpl: DictionaryImpl): Dictionary
}
