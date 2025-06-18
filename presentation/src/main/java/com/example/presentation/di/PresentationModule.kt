package com.example.presentation.di


import com.example.presentation.ViewModels.AllExercisesViewModel
import com.example.presentation.ViewModels.AllWorkoutsViewModel
import com.example.presentation.ViewModels.CreateWorkoutViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module{
    viewModel { AllExercisesViewModel(get()) }
    viewModel { AllWorkoutsViewModel(get()) }
    viewModel { CreateWorkoutViewModel(get(),get()) }
}