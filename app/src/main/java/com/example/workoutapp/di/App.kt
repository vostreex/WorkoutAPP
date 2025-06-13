package com.example.app

import android.app.Application
import com.example.data.database.DatabaseInit
import com.example.presentation.di.presentationModule
import com.example.workoutapp.di.appModule
import com.example.workoutapp.di.dataModule
import com.example.workoutapp.di.domainModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class MyApp : Application(), KoinComponent {
    private val databaseInit: DatabaseInit by inject()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(appModule, dataModule, domainModule,presentationModule))
        }
        coroutineScope.launch {
            databaseInit.initialize()
        }
    }
}