package com.example.clean.di.module

import com.example.clean.api.ApiService
import com.example.clean.data.localdb.ItemDatabase
import com.example.clean.data.repository.SummaryRepository
import com.example.clean.data.repository.SummaryRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideSummaryRepository(service: ApiService, database: ItemDatabase): SummaryRepository =
        SummaryRepositoryImpl(service, database)
}