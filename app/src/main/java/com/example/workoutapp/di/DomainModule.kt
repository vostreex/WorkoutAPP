package com.example.workoutapp.di

import com.example.domain.usecase.AddExercise
import com.example.domain.usecase.ExercisesUseCases
import com.example.domain.usecase.GetExercises
import org.koin.dsl.module

val domainModule = module {
    factory { GetExercises(get()) }
    factory { AddExercise(get()) }
    factory { ExercisesUseCases(get(),get()) }
}