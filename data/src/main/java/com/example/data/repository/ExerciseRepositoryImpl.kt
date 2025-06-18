package com.example.data.repository


import com.example.data.database.ExerciseDAO
import com.example.data.model.toDomain
import com.example.data.model.toEntity
import com.example.domain.model.Exercise
import com.example.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExerciseRepositoryImpl(private val exerciseDao: ExerciseDAO) : ExerciseRepository {
    override fun getAllExercises(): Flow<List<Exercise>> {
        return exerciseDao.getALL().map { exercises->
            exercises.map { it.toDomain() }
        }
    }

    override suspend fun addExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise.toEntity())
    }

    override suspend fun deleteExercise(id: Long) {
        exerciseDao.deleteExercise(id)
    }
}