package com.example.clean.di.module

import androidx.room.Room
import com.example.clean.CleanApp
import com.example.clean.data.localdb.ItemDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val app: CleanApp) {

    @Singleton
    @Provides
    fun providesDatabase(): ItemDatabase =
        Room.databaseBuilder(
            app.applicationContext,
            ItemDatabase::class.java,
            "item-db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideItemDao(db: ItemDatabase) = db.itemDao()
}