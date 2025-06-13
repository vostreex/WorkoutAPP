package com.example.data.database


import com.example.data.model.ExerciseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DatabaseInit(private val exerciseDao: ExerciseDAO) {
    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            val exercises = exerciseDao.getALL().first()
            if (exercises.isEmpty()) {
                val builtInExercises = listOf(
                    ExerciseEntity(name = "Жим лежа", muscleGroup = "Грудь", gifURL = "https://media.tenor.com/kpJH4zjuPF8AAAAM/supino.gif"),
                    ExerciseEntity(name = "Приседания", muscleGroup = "Ноги", gifURL = "https://instructorpro.ru/wp-content/uploads/2023/06/Приседания-сумо-Выполнение.gif   "),
                )
                exerciseDao.insertExercises(builtInExercises)
            }
        }
    }
}