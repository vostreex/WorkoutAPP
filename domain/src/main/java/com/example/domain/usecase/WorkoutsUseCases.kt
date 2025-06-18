package com.example.domain.usecase

data class WorkoutsUseCases(
    val getWorkouts: GetWorkouts,
    val getExercisesFromWorkout: GetExercisesFromWorkout,
    val createWorkout: CreateWorkout,
    val deleteWorkout: DeleteWorkout
)