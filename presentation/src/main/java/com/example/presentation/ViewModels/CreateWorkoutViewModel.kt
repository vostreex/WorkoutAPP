package com.example.presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import com.example.domain.usecase.ExercisesUseCases
import com.example.domain.usecase.WorkoutsUseCases
import com.example.presentation.States.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreateWorkoutViewModel(
    private val exercisesUseCases: ExercisesUseCases,
    private val WorkoutUseCases: WorkoutsUseCases
): ViewModel() {

    private val _exercisesList = MutableStateFlow<State<List<Exercise>>>(State.Loading)
    val exercisesList: StateFlow<State<List<Exercise>>> = _exercisesList

    init {
        getAllExercises()
    }

    fun getAllExercises(filter: String? = null){
        viewModelScope.launch {
            _exercisesList.value = State.Loading
            exercisesUseCases.getExercises(filter)
                .catch{e ->
                    _exercisesList.value = State.Error(e.message)
                    println("Error loading exercises: ${e.message}")
                }
                .collect {exercises ->
                    _exercisesList.value = State.Success(exercises)
                }
        }
    }

    fun createWorkout(name: String,exercises: Set<Exercise>){
        viewModelScope.launch {
            val idsList = exercises.map {
                it.id
            }
            WorkoutUseCases.createWorkout(Workout(name = name, exercisesIdList = idsList))
        }
    }

}