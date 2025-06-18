package com.example.workoutapp.di

import com.example.domain.usecase.AddExercise
import com.example.domain.usecase.CreateWorkout
import com.example.domain.usecase.DeleteExercise
import com.example.domain.usecase.DeleteWorkout
import com.example.domain.usecase.ExercisesUseCases
import com.example.domain.usecase.GetExercises
import com.example.domain.usecase.GetExercisesFromWorkout
import com.example.domain.usecase.GetWorkouts
import com.example.domain.usecase.WorkoutsUseCases
import org.koin.dsl.module

val domainModule = module {
    factory { GetExercises(get()) }
    factory { AddExercise(get()) }
    factory { DeleteExercise(get()) }
    factory { ExercisesUseCases(get(),get(),get()) }
    factory { GetWorkouts(get()) }
    factory { GetExercisesFromWorkout(get()) }
    factory { CreateWorkout(get()) }
    factory { DeleteWorkout(get()) }
    factory { WorkoutsUseCases(get(),get(),get(),get()) }
}