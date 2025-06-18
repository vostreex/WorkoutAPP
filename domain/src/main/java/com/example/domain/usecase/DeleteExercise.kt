package com.example.domain.usecase

import com.example.domain.repository.ExerciseRepository

class DeleteExercise(
    val repository: ExerciseRepository
) {
    suspend operator fun invoke(id: Long){
        repository.deleteExercise(id)
    }
}