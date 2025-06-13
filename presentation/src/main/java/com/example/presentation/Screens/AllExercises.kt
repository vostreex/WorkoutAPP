package com.example.presentation.Screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.Exercise
import com.example.presentation.States.State
import com.example.presentation.ViewModels.AllExercisesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllExercises(
    modifier: Modifier = Modifier,
    viewModel: AllExercisesViewModel = koinViewModel<AllExercisesViewModel>()
){

    val exercisesList = viewModel.exercisesList.collectAsState().value
    Column (
        modifier = modifier
            .fillMaxSize()
    ){
        when(exercisesList){
            is State.Error -> Text("ERROR: ${exercisesList.message ?: "Unknown error"}")
            State.Loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            is State.Success ->  LazyColumn {
                items(exercisesList.data) {
                    ExerciseCard(it)
                }
            }
        }
    }
}


@Composable
fun ExerciseCard(exercise: Exercise){
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        onClick = {}
    ) {
        Column(

        ) {
            Text(exercise.name)
            Text("Группа мышц: ${exercise.muscleGroup}")
        }
    }
}

