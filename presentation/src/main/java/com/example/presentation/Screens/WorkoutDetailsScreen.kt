package com.example.presentation.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import com.example.presentation.ViewModels.WorkoutDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.presentation.States.State

@Composable
fun WorkoutDetailsScreen(
    workoutId: Long,
    viewModel: WorkoutDetailsViewModel = koinViewModel<WorkoutDetailsViewModel>(),
    navController: NavController,
) {
    val workoutState by viewModel.workout.collectAsState()
    val exercisesState by viewModel.exercises.collectAsState()

    LaunchedEffect(workoutId) {
        viewModel.loadWorkout(workoutId)
    }

    when (workoutState) {

        is State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is State.Error -> {
            Text("Ошибка: ${(workoutState as State.Error).message}")
        }

        is State.Success -> {
            val workout = (workoutState as State.Success<Workout>).data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Упражнения",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                when (exercisesState) {

                    is State.Loading -> {
                        CircularProgressIndicator()
                    }

                    is State.Error -> {
                        Text("Ошибка загрузки упражнений")
                    }

                    is State.Success -> {
                        val exercises = (exercisesState as State.Success<List<Exercise>>).data

                        LazyColumn {
                            items(exercises) { exercise ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable {
                                            navController.navigate(
                                                ExerciseInfo(
                                                    name = exercise.name,
                                                    muscleGroup = exercise.muscleGroup,
                                                    aboutExercise = exercise.aboutExercise,
                                                    gifURL = exercise.gifURL
                                                )
                                            )
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Text(
                                            text = exercise.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = exercise.muscleGroup,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}