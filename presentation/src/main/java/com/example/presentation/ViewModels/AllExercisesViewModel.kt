package com.example.presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Exercise
import com.example.domain.usecase.ExercisesUseCases
import com.example.domain.usecase.GetExercises
import com.example.presentation.States.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AllExercisesViewModel(
    private val exercisesUseCases: ExercisesUseCases
): ViewModel() {


    private val _exercisesList = MutableStateFlow<State<List<Exercise>>>(State.Loading)
    val exercisesList: StateFlow<State<List<Exercise>>> = _exercisesList

    init {
        getAllExercises()
    }

    fun getAllExercises(){
        viewModelScope.launch {
            _exercisesList.value = State.Loading
            exercisesUseCases.getExercises()
                .catch{e ->
                    _exercisesList.value = State.Error(e.message)
                    println("Error loading exercises: ${e.message}")
                }
                .collect {exercises ->
                    _exercisesList.value = State.Success(exercises)
                }
        }
    }

    fun refresh() {
        getAllExercises()
    }

}