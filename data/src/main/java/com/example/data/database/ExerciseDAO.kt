package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.model.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDAO {
    @Query("SELECT * FROM exercises")
    fun getALL(): Flow<List<ExerciseEntity>>

    @Insert
    suspend fun insertExercise(exercise: ExerciseEntity)

    @Insert
    suspend fun insertExercises(exercises: List<ExerciseEntity>)
}