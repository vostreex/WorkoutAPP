package com.example.presentation.di


import com.example.presentation.ViewModels.AllExercisesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module{
    viewModel { AllExercisesViewModel(get()) }
}