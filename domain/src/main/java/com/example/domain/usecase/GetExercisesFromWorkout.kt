package com.example.domain.usecase

import com.example.domain.model.Exercise
import com.example.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExercisesFromWorkout(
    private val repository: WorkoutRepository
) {
    operator fun invoke(
        ids: List<Long>
    ) :  Flow<List<Exercise>>{
        return repository.getExercises(ids)
    }
}