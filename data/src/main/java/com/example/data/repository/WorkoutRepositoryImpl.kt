package com.example.data.repository

import com.example.data.database.WorkoutDAO
import com.example.data.model.toDomain
import com.example.data.model.toEntity
import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl(
    private val workoutDAO: WorkoutDAO
): WorkoutRepository {
    override fun getWorkouts(): Flow<List<Workout>> {
        return workoutDAO.getAllWorkouts().map { workouts ->
            workouts.map {
                it.toDomain()
            }
        }
    }

    override fun getExercises(ids: List<Long>): Flow<List<Exercise>> {
        return workoutDAO.getExercisesById(ids).map { exercises->
            exercises.map {
                it.toDomain()
            }
        }
    }

    override suspend fun createWorkOut(
        workout: Workout
    ) {
        workoutDAO.insertWorkout(workout.toEntity())
    }

    override suspend fun deleteWorkOut(id: Long) {
        workoutDAO.deleteWorkout(id)
    }
}