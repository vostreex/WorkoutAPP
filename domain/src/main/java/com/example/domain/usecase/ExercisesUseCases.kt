package com.example.domain.usecase

import com.example.domain.model.Exercise

data class ExercisesUseCases(
    val getExercises: GetExercises,
    val addExercise: AddExercise,
    val deleteExercise: DeleteExercise
)