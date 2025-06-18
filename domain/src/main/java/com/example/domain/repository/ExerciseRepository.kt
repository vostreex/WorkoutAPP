package com.example.domain.repository


import com.example.domain.model.Exercise
import kotlinx.coroutines.flow.Flow


interface ExerciseRepository {
    fun getAllExercises(): Flow<List<Exercise>>
    suspend fun addExercise(exercise: Exercise)
    suspend fun deleteExercise(id: Long)
}