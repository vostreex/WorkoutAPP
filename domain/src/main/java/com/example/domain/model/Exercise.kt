package com.example.domain.model

data class Exercise(
    val id: Long = 0,
    val name: String,
    val muscleGroup: String,
    val gifURL: String?
)
