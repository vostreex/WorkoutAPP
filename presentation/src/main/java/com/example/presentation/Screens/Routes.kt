package com.example.presentation.Screens

import com.example.domain.model.Exercise
import kotlinx.serialization.Serializable

@Serializable
object AllExercisesScreen

@Serializable
object AllWorkoutsScreen

@Serializable
data class ExerciseInfo(
    val name: String,
    val muscleGroup: String,
    val gifURL: String? = null,
    val aboutExercise: String? = null
)