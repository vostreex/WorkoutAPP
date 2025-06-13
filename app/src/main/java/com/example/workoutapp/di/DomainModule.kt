package com.example.workoutapp.di

import com.example.domain.repository.ExerciseRepository
import com.example.domain.usecase.ExercisesUseCases
import com.example.domain.usecase.GetExercises
import org.koin.dsl.module

val domainModule = module {
    factory { GetExercises(get()) }
    factory { ExercisesUseCases(get()) }
}