package com.example.clean

import android.app.Application
import com.example.clean.di.component.AppComponent
import com.example.clean.di.component.DaggerAppComponent
import com.example.clean.di.module.AppModule
import com.example.clean.di.module.DatabaseModule

class CleanApp: Application() {

    // Reference to the application dependency graph which is used across the whole app
    val cleanAppComponent: AppComponent = DaggerAppComponent.builder()
        .application(this)
        .appModule(AppModule(this))
        .databaseModule(DatabaseModule(this))
        .build()
}