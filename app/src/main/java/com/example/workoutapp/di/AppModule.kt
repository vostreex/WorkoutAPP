package com.example.workoutapp.di

import androidx.room.Room
import com.example.data.database.AppDatabase
import com.example.data.database.DatabaseInit
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "workout-db"
        ).build()
    }
    single { get<AppDatabase>().exerciseDao() }
    single { get<AppDatabase>().workoutDao() }
    single { DatabaseInit(get(),get()) }
}