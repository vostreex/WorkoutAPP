package com.example.domain.model

data class Workout (
    val id: Long = 0,
    val name: String,
    val exercisesIdList: List<Long>
)
