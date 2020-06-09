package com.example.clean.di.module

import com.example.clean.api.AzureFunctionService
import com.example.clean.data.localdb.ItemDatabase
import com.example.clean.data.repository.*
import com.example.clean.data.sources.AzureItemDataSource
import com.example.clean.data.sources.ItemLocalDataSource
import com.example.clean.data.sources.ItemRemoteDataSource
import com.example.clean.data.sources.RoomItemDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideSummaryRepository(remoteDataSource: ItemRemoteDataSource, localDataSource: ItemLocalDataSource): SummaryRepository =
        SummaryRepositoryRemoteCached(remoteDataSource, localDataSource)

    @Singleton
    @Provides
    fun provideItemRemoteDataSource(service: AzureFunctionService): ItemRemoteDataSource =
        AzureItemDataSource(service)

    @Singleton
    @Provides
    fun provideItemLocalDataSource(database: ItemDatabase): ItemLocalDataSource =
        RoomItemDataSource(database)
}