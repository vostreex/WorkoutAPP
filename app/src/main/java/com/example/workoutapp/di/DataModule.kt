package com.example.workoutapp.di

import com.example.data.repository.ExerciseRepositoryImpl
import com.example.domain.repository.ExerciseRepository
import org.koin.dsl.module

val dataModule = module {
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
}