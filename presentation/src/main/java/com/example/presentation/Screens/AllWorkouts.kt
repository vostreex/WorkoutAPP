package com.example.presentation.Screens

import android.R
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.domain.model.Exercise
import com.example.domain.model.Workout
import com.example.presentation.States.State
import com.example.presentation.ViewModels.AllWorkoutsViewModel
import com.example.presentation.ViewModels.CreateWorkoutViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllWorkouts(
    modifier: Modifier = Modifier,
    viewModel: AllWorkoutsViewModel = koinViewModel<AllWorkoutsViewModel>()
){
    val workoutsList = viewModel.workoutsList.collectAsState().value
    val exercises = viewModel.exerciseForWorkout.collectAsState().value
    var finder by rememberSaveable { mutableStateOf("") }
    var selectedItem: Long by rememberSaveable { mutableStateOf(-1) }
    var showModalBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier.fillMaxSize()
    ) {
        TextField(
            value = finder,
            onValueChange = {finder = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clip(CircleShape),
            label = { Text("Поиск")},
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        when(workoutsList){
            is State.Error -> Text("ERROR: ${workoutsList.message ?: "Unknown error"}")
            State.Loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            is State.Success -> LazyColumn {
                val filteredWorkouts = if(finder.isNotBlank()){
                    workoutsList.data.filter { it.name.lowercase().contains(finder.lowercase()) }
                } else{
                    workoutsList.data
                }
                items(filteredWorkouts){
                    val isSelected = it.id == selectedItem
                    WorkoutsItems(
                        workout = it,
                        isSelected = isSelected,
                        exercises = exercises[it.id],
                        onSwipeLeft = {
                            selectedItem = -1
                        },
                        onSwipeRight = {
                            selectedItem = it.id
                        },
                        onDeleteClick = {
                            viewModel.deleteWorkout(selectedItem)
                            selectedItem = -1
                        },
                        onShowCardClick = {

                        }
                    )
                }
            }
        }
    }
    Row(
        modifier
            .fillMaxSize()
            .background(Color.Transparent),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = {
                showModalBottomSheet = true
            },
            modifier = Modifier.clip(CircleShape).size(80.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu_add),
                contentDescription = "addButton",
            )
        }
    }
    if (showModalBottomSheet) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ){
                    showModalBottomSheet = false
                }
        ) {
            CreateWorkout()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWorkout(
    viewModel: CreateWorkoutViewModel = koinViewModel<CreateWorkoutViewModel>()
) {
    val exercisesList = viewModel.exercisesList.collectAsState().value
    var name by remember { mutableStateOf("") }
    var selectedExercises by remember { mutableStateOf<Set<Exercise>>(emptySet()) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(16.dp).clickable(onClick = {}),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Поле для названия тренировки
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    label = { Text("Название тренировки") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                // Отображение выбранных упражнений
                if (selectedExercises.isNotEmpty()) {
                    Text(
                        text = "Выбрано упражнений: ${selectedExercises.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .heightIn(max = 150.dp)
                    ) {
                        items(selectedExercises.toList()) { exercise ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .clickable(
                                        onClick = {
                                            selectedExercises -= exercise
                                        }
                                    )
                            ) {
                                Text(
                                    text = "${exercise.name} (${exercise.muscleGroup})",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Карточка-кнопка для открытия списка упражнений
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch { sheetState.show() }
                            showSheet = true
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Добавить упражнения",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (name.isNotBlank() && selectedExercises.isNotEmpty()) {
                            viewModel.createWorkout(name,selectedExercises)
                            name = ""
                            selectedExercises = emptySet()
                        } else {
                            Toast.makeText(context, "Обязательные поля должны быть заполнены", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Создать",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // Модальное окно с списком упражнений
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { sheetState.hide() }
                    showSheet = false
                },
                sheetState = sheetState
            ) {
                var searchQuery by remember { mutableStateOf("") }
                var selectedCategory by remember { mutableStateOf("Все") }

                // Список категорий для фильтрации
                val categories = listOf("Все") + (exercisesList as? State.Success)?.data?.map { it.muscleGroup }?.distinct().orEmpty()

                Column(modifier = Modifier.padding(16.dp)) {
                    // Поле поиска
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(CircleShape),
                        label = { Text("Поиск по имени") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )

                    // Выпадающий список для категорий
                    var isCategoryMenuExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = isCategoryMenuExpanded,
                        onExpandedChange = { isCategoryMenuExpanded = it }
                    ) {
                        TextField(
                            value = selectedCategory,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .menuAnchor(),
                            readOnly = true,
                            label = { Text("Категория") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded) },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        DropdownMenu(
                            expanded = isCategoryMenuExpanded,
                            onDismissRequest = { isCategoryMenuExpanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        isCategoryMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    // Список упражнений
                    when (exercisesList) {
                        is State.Loading -> Text("Загрузка...")
                        is State.Error -> Text("Ошибка: ${exercisesList.message ?: "Неизвестная ошибка"}")
                        is State.Success -> {
                            val filteredExercises = exercisesList.data.filter {
                                (selectedCategory == "Все" || it.muscleGroup == selectedCategory) &&
                                        it.name.contains(searchQuery, ignoreCase = true)
                            }
                            LazyColumn {
                                items(filteredExercises) { exercise ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedExercises = if (selectedExercises.contains(exercise)) {
                                                    selectedExercises - exercise
                                                } else {
                                                    selectedExercises + exercise
                                                }
                                            }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${exercise.name} (${exercise.muscleGroup})",
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (selectedExercises.contains(exercise)) {
                                            Text("✓", color = Color.Green)
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
}

@Composable
fun WorkoutsItems(
    workout: Workout,
    exercises: List<Exercise>?,
    isSelected: Boolean,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onDeleteClick: () -> Unit,
    onShowCardClick: () -> Unit
){
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    val uniqueMuscleGroup = exercises?.map {
        it.muscleGroup
    }?.toSet()
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .offset { IntOffset(offsetX.toInt(), 0) }
            .pointerInput(Unit){
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if(offsetX > size.width / 3){
                            onSwipeLeft()
                        } else if(offsetX < -size.width / 3){
                            onSwipeRight()
                        }
                        offsetX = 0f
                    },
                    onHorizontalDrag = {change,dragAmount ->
                        change.consumeAllChanges()
                        offsetX += dragAmount
                    }
                )
            },
        onClick = {
            onShowCardClick()
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
        ) {
            Column{
                Text(workout.name)
                Row {
                    Text("Группы мышц: ")
                    uniqueMuscleGroup?.map {
                        Text(it, modifier = Modifier.padding(end = 8.dp))
                    }
                }
            }
            if (isSelected){
                IconButton(
                    onClick = {
                        onDeleteClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_menu_delete),
                        contentDescription = "deleteIcon"
                    )
                }
            }
        }
    }
}
