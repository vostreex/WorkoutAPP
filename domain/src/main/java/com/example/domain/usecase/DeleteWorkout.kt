package com.example.domain.usecase

import com.example.domain.repository.WorkoutRepository

class DeleteWorkout(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(id: Long){
        repository.deleteWorkOut(id)
    }
}