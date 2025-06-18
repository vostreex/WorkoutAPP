package com.example.domain.usecase

import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository

class CreateWorkout(
    private val repository: WorkoutRepository
) {
     suspend operator fun invoke(
         workout: Workout
     ){
         repository.createWorkOut(workout)
     }
}