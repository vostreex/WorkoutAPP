package com.example.data.database


import com.example.data.model.ExerciseEntity
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DatabaseInit(private val exerciseDao: ExerciseDAO,private val workoutDAO: WorkoutDAO) {
    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            val exercises = exerciseDao.getALL().first()
            val workouts = workoutDAO.getAllWorkouts().first()
            if (exercises.isEmpty()) {
                val builtInExercises = listOf(
                    ExerciseEntity(name = "Жим лежа", muscleGroup = "Грудь", gifURL = "https://media.tenor.com/kpJH4zjuPF8AAAAM/supino.gif", aboutExercise = null),
                    ExerciseEntity(name = "Приседания", muscleGroup = "Ноги", gifURL = "https://instructorpro.ru/wp-content/uploads/2023/06/Приседания-сумо-Выполнение.gif   ", aboutExercise = null),
                )
                exerciseDao.insertExercises(builtInExercises)
            }
            if (workouts.isEmpty()){
                val builtInExercises = listOf(
                    WorkoutEntity(name = "Первая", exercisesIdList = listOf(1,2)),
                )
                workoutDAO.insertWorkouts(builtInExercises)
            }
        }
    }
}