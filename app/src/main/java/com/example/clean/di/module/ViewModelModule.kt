package com.example.clean.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clean.ViewModelFactory
import com.example.clean.di.annotation.ViewModelKey
import com.example.clean.ui.summarylist.SummaryListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SummaryListViewModel::class)
    abstract fun bindSummaryListViewModel(summaryListViewModel: SummaryListViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}