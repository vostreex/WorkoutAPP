package com.example.domain.usecase

import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkouts(
    private val repository: WorkoutRepository
) {
    operator fun invoke() : Flow<List<Workout>> {
        return repository.getWorkouts().map { it.sortedBy { it.name } }
    }
}