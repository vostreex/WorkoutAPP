package com.example.domain.usecase

import com.example.domain.model.Exercise
import com.example.domain.repository.ExerciseRepository

class AddExercise(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(name: String,muscleGroup: String, image: String?){
        repository.addExercise(Exercise(
            name = name,
            muscleGroup = muscleGroup,
            gifURL = image
            )
        )
    }
}