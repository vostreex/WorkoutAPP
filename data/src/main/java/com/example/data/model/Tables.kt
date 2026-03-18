package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import kotlin.String

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val muscleGroup: String,
    val gifURL: String?,
    val aboutExercise: String?
)

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val exercisesIdList: List<Long>
)


fun ExerciseEntity.toDomain() = Exercise(
    id = id,
    name = name,
    muscleGroup = muscleGroup,
    gifURL = gifURL,
    aboutExercise = aboutExercise
)

fun Exercise.toEntity() = ExerciseEntity(
    name = name,
    muscleGroup = muscleGroup,
    gifURL = gifURL,
    aboutExercise = aboutExercise
)

fun WorkoutEntity.toDomain() = Workout(
    id = id,
    name = name,
    exercisesIdList = exercisesIdList
)

fun Workout.toEntity() = WorkoutEntity(
    name = name,
    exercisesIdList = exercisesIdList
)