package com.example.workoutapp.di

import com.example.data.repository.ExerciseRepositoryImpl
import com.example.data.repository.WorkoutRepositoryImpl
import com.example.domain.repository.ExerciseRepository
import com.example.domain.repository.WorkoutRepository
import org.koin.dsl.module

val dataModule = module {
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get()) }
}