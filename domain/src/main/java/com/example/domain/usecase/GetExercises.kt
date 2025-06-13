package com.example.domain.usecase

import com.example.domain.model.Exercise
import com.example.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExercises(
    private val repository: ExerciseRepository
) {
    operator fun invoke() : Flow<List<Exercise>>{
        return repository.getAllExercises().map { it.sortedBy { it.name } }
    }
}