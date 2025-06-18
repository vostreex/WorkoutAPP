package com.example.domain.usecase

import com.example.domain.model.Exercise
import com.example.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class GetExercises(
    private val repository: ExerciseRepository
) {
    operator fun invoke(
        filter : String?
    ) : Flow<List<Exercise>>{
        return if(filter != null){
            repository.getAllExercises().map { it.sortedBy { it.name }.filter { it.muscleGroup.lowercase().equals(filter.lowercase()) } }
        } else repository.getAllExercises().map { it.sortedBy { it.name } }
    }
}