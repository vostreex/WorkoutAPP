package com.example.presentation.Screens


import android.R

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.example.domain.model.Exercise
import com.example.presentation.States.State
import com.example.presentation.ViewModels.AllExercisesViewModel
import org.koin.androidx.compose.koinViewModel
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.size.Size
import java.io.File
import java.nio.file.WatchEvent


@Composable
fun AllExercises(
    modifier: Modifier = Modifier,
    viewModel: AllExercisesViewModel = koinViewModel<AllExercisesViewModel>(),
    navController: NavController
){
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )
    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
    )
    val exercisesList = viewModel.exercisesList.collectAsState().value
    var showModalBottomSheet by rememberSaveable { mutableStateOf(false) }
    var finder by rememberSaveable { mutableStateOf("") }
    var selectedItem: Long by rememberSaveable { mutableStateOf(-1) }
    val listOfCategory = viewModel.exercisesCategoryList.collectAsState().value
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedCategory) {
        viewModel.getAllExercises(selectedCategory)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .alpha(animatedAlpha)
            .scale(animatedScale)
    ) {
        TextField(
            value = finder,
            onValueChange = {finder = it},
            modifier = Modifier.fillMaxWidth().padding(5.dp).clip(CircleShape),
            label = { Text("Поиск")},
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        LazyRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(5.dp).fillMaxWidth()
        ) {
            if(selectedCategory != null){
                item {
                    Text("Все",modifier = Modifier.clickable(onClick = {
                        selectedCategory = null
                    }))
                }
            }
            items(listOfCategory) {
                Text(it,modifier = Modifier.clickable(onClick = {
                    selectedCategory = it
                }).padding(end = 8.dp))
            }
        }
        when (exercisesList) {
            is State.Error -> Text("ERROR: ${exercisesList.message ?: "Unknown error"}")
            State.Loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            is State.Success -> LazyColumn {
                val filteredExercises = if(finder.isNotBlank()){
                    exercisesList.data.filter { it.name.lowercase().contains(finder.lowercase()) }
                } else{
                    exercisesList.data
                }
                if (filteredExercises.isEmpty()){
                    item{
                        Text(
                            text = "Упраженения не найдены",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                } else{
                    items(filteredExercises) {
                        val isSelected = it.id == selectedItem
                        ExerciseCard(
                            exercise = it,
                            isSelected = isSelected,
                            onSwipeLeft = {
                                selectedItem = -1
                            },
                            onSwipeRight = {
                                selectedItem = it.id
                            },
                            onDeleteClick = {
                                viewModel.deleteExercise(selectedItem)
                                selectedItem = -1
                            },
                            onShowCardClick = {
                                navController.navigate(ExerciseInfo(
                                    name = it.name,
                                    muscleGroup = it.muscleGroup,
                                    aboutExercise = it.aboutExercise,
                                    gifURL = it.gifURL
                                ))
                            }
                        )
                    }
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
                .alpha(animatedAlpha)
                .scale(animatedScale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ){
                    showModalBottomSheet = false
                }
        ) {
            CreateExercise(viewModel)
        }
    }
}


@Composable
fun ExerciseCard(
    exercise: Exercise,
    isSelected: Boolean,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onDeleteClick: () -> Unit,
    onShowCardClick: () -> Unit
){
    var offsetX by rememberSaveable { mutableStateOf(0f) }
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
                Text(exercise.name)
                Text("Группа мышц: ${exercise.muscleGroup}")
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


@Composable
fun CreateExercise(
    viewModel: AllExercisesViewModel,
    modifier: Modifier = Modifier
) {
    // Состояния для полей ввода и изображения
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    var name by rememberSaveable { mutableStateOf("") }
    var muscleGroup by rememberSaveable { mutableStateOf("") }
    var aboutExercise by rememberSaveable { mutableStateOf("") }

    // Анимация для карточки
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )
    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
    )

    // Основной контейнер для центрирования
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .alpha(animatedAlpha)
                .scale(animatedScale)
                .padding(16.dp)
                .clickable(onClick = {}),
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Изображение
                imageUri?.let {
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(it)
                            .size(Size.ORIGINAL)
                            .build()
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Изображение упражнения",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.size(16.dp))
                }

                // Поле для названия
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    label = { Text("Название упражнения") },
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
                Spacer(Modifier.size(12.dp))

                // Поле для группы мышц
                TextField(
                    value = muscleGroup,
                    onValueChange = { muscleGroup = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    label = { Text("Группа мышц") },
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
                Spacer(Modifier.size(12.dp))

                // Поле для описания
                TextField(
                    value = aboutExercise,
                    onValueChange = { aboutExercise = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    label = { Text("Описание") },
                    maxLines = 3,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
                Spacer(Modifier.size(16.dp))

                // Кнопки
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { launcher.launch("image/*") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = "Выбрать фото",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Button(
                        onClick = {
                            if (name.isNotBlank() && muscleGroup.isNotBlank()) {
                                val imagePath = imageUri?.let { uri ->
                                    copyUriToFile(context, uri)?.absolutePath
                                }
                                viewModel.addExercise(name, muscleGroup, imagePath, aboutExercise)
                                name = ""
                                muscleGroup = ""
                                aboutExercise = ""
                                imageUri = null
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
        }
    }
}

fun copyUriToFile(context: Context, uri: Uri): File? {
    return try {
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val extension = when (mimeType) {
            "image/gif" -> "gif"
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            else -> "jpg"
        }
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "image_${System.currentTimeMillis()}.$extension")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        println("Error copying URI: $e")
        null
    }
}