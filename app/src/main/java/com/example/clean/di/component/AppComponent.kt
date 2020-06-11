package com.example.clean.di.component

import android.app.Application
import com.example.clean.di.module.*
import com.example.clean.ui.fragments.content.ContentFragment
import com.example.clean.ui.fragments.summarylist.SummaryListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DatabaseModule::class, RepositoryModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun appModule(appModule: AppModule): Builder
        fun databaseModule(databaseModule: DatabaseModule): Builder
        fun build(): AppComponent
    }

    fun inject(summaryListFragment: SummaryListFragment)
    fun inject(contentFragment: ContentFragment)
}