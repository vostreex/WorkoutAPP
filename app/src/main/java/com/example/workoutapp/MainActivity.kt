package com.example.workoutapp

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.domain.model.Exercise
import com.example.presentation.Screens.AllExercises
import com.example.presentation.Screens.AllExercisesScreen
import com.example.presentation.Screens.AllWorkouts
import com.example.presentation.Screens.AllWorkoutsScreen
import com.example.presentation.Screens.ExerciseCard
import com.example.presentation.Screens.ExerciseInfo
import com.example.presentation.Screens.ShowExerciseCard
import com.example.presentation.Screens.TopBar
import com.example.presentation.Screens.WorkoutDetailsRoute
import com.example.presentation.Screens.WorkoutDetailsScreen
import com.example.workoutapp.ui.theme.WorkoutAPPTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val currentBackStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry.value?.destination?.route
            WorkoutAPPTheme(
                darkTheme = isSystemInDarkTheme(),
            ) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            TopBar(
                                navController,
                                drawerState
                            )
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                title = {
                                    when (currentRoute) {
                                        AllExercisesScreen::class.qualifiedName -> Text("Упражнения")
                                        AllWorkoutsScreen::class.qualifiedName -> Text("Тренировки")
                                        else -> Text("Приложение для тренировок")
                                    }
                                },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        },
                                        modifier = Modifier.clip(CircleShape).size(80.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )

                                    }
                                }
                            )
                        },
                    ) { innerPadding ->
                        NavHost(navController = navController, startDestination = AllExercisesScreen){
                            composable<AllExercisesScreen> {
                                AllExercises(
                                    modifier = Modifier.padding(innerPadding),
                                    navController = navController
                                )
                            }
                            composable<AllWorkoutsScreen> {
                                AllWorkouts(
                                    modifier = Modifier.padding(innerPadding),
                                    navController = navController
                                    )
                            }
                            composable<ExerciseInfo> { currentBackStackEntry->
                                val name = currentBackStackEntry.toRoute<ExerciseInfo>().name
                                val muscleGroup = currentBackStackEntry.toRoute<ExerciseInfo>().muscleGroup
                                val aboutExercise = currentBackStackEntry.toRoute<ExerciseInfo>().aboutExercise
                                val gifURL = currentBackStackEntry.toRoute<ExerciseInfo>().gifURL
                                ShowExerciseCard(
                                    modifier = Modifier.padding(innerPadding),
                                    exercise = Exercise(name = name, muscleGroup = muscleGroup, aboutExercise = aboutExercise, gifURL = gifURL)
                                )
                            }
                            composable<WorkoutDetailsRoute> { backStackEntry ->
                                val workoutId = backStackEntry.toRoute<WorkoutDetailsRoute>().workoutId
                                WorkoutDetailsScreen(
                                    workoutId = workoutId,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
