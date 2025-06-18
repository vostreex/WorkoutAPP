package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.model.ExerciseEntity
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM exercises WHERE id IN (:ids)")
    fun getExercisesById(ids: List<Long>): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    @Insert
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Insert
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM workouts WHERE id = :id")
    suspend fun deleteWorkout(id: Long)

}