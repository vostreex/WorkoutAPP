package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.ExerciseEntity

import com.example.data.model.WorkoutEntity

@Database(entities = [ExerciseEntity::class, WorkoutEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDAO
    abstract fun workoutDao(): WorkoutDAO
}


