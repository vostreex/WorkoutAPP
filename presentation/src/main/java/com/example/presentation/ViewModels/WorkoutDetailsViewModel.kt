package com.example.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.domain.model.Workout
import com.example.domain.usecase.WorkoutsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Exercise
import com.example.presentation.States.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WorkoutDetailsViewModel(
    private val useCases: WorkoutsUseCases
) : ViewModel() {

    private val _workout = MutableStateFlow<State<Workout>>(State.Loading)
    val workout: StateFlow<State<Workout>> = _workout

    private val _exercises = MutableStateFlow<State<List<Exercise>>>(State.Loading)
    val exercises: StateFlow<State<List<Exercise>>> = _exercises

    fun loadWorkout(id: Long) {
        viewModelScope.launch {
            _workout.value = State.Loading

            useCases.getWorkout(id)
                .catch { e ->
                    _workout.value = State.Error(e.message)
                }
                .collect { workout ->

                    _workout.value = State.Success(workout)

                    loadExercises(workout.exercisesIdList)
                }
        }
    }

    private fun loadExercises(ids: List<Long>) {
        viewModelScope.launch {
            _exercises.value = State.Loading

            useCases.getExercisesFromWorkout(ids)
                .catch { e ->
                    _exercises.value = State.Error(e.message)
                }
                .collect { list ->
                    _exercises.value = State.Success(list)
                }
        }
    }
}