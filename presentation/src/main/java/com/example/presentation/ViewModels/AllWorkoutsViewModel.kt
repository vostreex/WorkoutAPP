package com.example.presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import com.example.domain.usecase.WorkoutsUseCases
import com.example.presentation.States.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AllWorkoutsViewModel(
    private val useCases: WorkoutsUseCases
): ViewModel() {

    private val _workoutsList = MutableStateFlow<State<List<Workout>>>(State.Loading)
    val workoutsList: StateFlow<State<List<Workout>>> = _workoutsList

    private val _exerciseForWorkout = MutableStateFlow<Map<Long,List<Exercise>>>(emptyMap())
    val exerciseForWorkout: StateFlow<Map<Long,List<Exercise>>> = _exerciseForWorkout

    init {
        getALlWorkout()
    }

    fun getALlWorkout(){
        viewModelScope.launch {
            _workoutsList.value = State.Loading
            useCases.getWorkouts()
                .catch {e->
                    _workoutsList.value = State.Error(e.message)
                }
                .collect { workouts->
                    _workoutsList.value = State.Success(workouts)
                    workouts.forEach { workout ->
                        getExercises(workout.id, workout.exercisesIdList)
                    }
            }
        }
    }

    fun getExercises(workoutId : Long,ids: List<Long>){
        viewModelScope.launch {
            useCases.getExercisesFromWorkout(ids).collect {
                _exerciseForWorkout.value = _exerciseForWorkout.value.toMutableMap().apply {
                    this[workoutId] = it
                }
            }
        }
    }

    fun deleteWorkout(id: Long){
        viewModelScope.launch {
            useCases.deleteWorkout(id)
        }
    }
}