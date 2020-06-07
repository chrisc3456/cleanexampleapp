package com.example.clean.di.module

import com.example.clean.CleanApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: CleanApp) {

    @Singleton
    @Provides
    fun provideApp() = app
}