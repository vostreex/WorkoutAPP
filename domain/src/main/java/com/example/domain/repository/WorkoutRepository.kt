package com.example.domain.repository

import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(): Flow<List<Workout>>
    fun getExercises(ids: List<Long>) :  Flow<List<Exercise>>
    suspend fun createWorkOut(workout: Workout)
    suspend fun deleteWorkOut(id: Long)
    fun getWorkOutById(id: Long): Flow<Workout>
}