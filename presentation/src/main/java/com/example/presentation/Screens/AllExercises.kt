package com.example.presentation.Screens


import android.R

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
    viewModel: AllExercisesViewModel = koinViewModel<AllExercisesViewModel>()
){

    val exercisesList = viewModel.exercisesList.collectAsState().value
    var showModalBottomSheet by rememberSaveable { mutableStateOf(false) }
    var finder by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TextField(
            value = finder,
            onValueChange = {finder = it},
            modifier = Modifier.fillMaxWidth()
        )
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
                        ExerciseCard(it)
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
fun ExerciseCard(exercise: Exercise){
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        onClick = {}
    ) {
        Column(

        ) {
            exercise.gifURL?.let { GifImage(
                data = it,
                modifier =  Modifier.height(150.dp).fillMaxWidth(),
                contentDescription = "Animated GIF"
            ) }
            Text(exercise.name)
            Text("Группа мышц: ${exercise.muscleGroup}")
        }
    }
}

@Composable
fun GifImage(
    data: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    errorResId: Int = R.drawable.stat_notify_error
) {
    val context = LocalContext.current
    val hasError = rememberSaveable { mutableStateOf(false) }

    val isUrl = data.startsWith("http://") || data.startsWith("https://")
    val imageData: Any? = if (isUrl) {
        data
    } else {
        val file = File(data)
        if (file.exists()) {
            file
        } else {
            null
        }
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageData)
            .apply {
                when {
                    imageData is String && imageData.trim().endsWith(".gif") -> {
                        decoderFactory(GifDecoder.Factory())
                    }
                    imageData is File && imageData.name.endsWith(".gif") -> {
                        decoderFactory(GifDecoder.Factory())
                    }
                }
            }
            .size(Size.ORIGINAL)
            .listener(
                onError = { _, _ ->
                    hasError.value = true
                }
            )
            .build()
    )
    Box(modifier = modifier) {
        when {
            hasError.value -> {
                Image(
                    painter = painterResource(id = errorResId),
                    contentDescription = contentDescription ?: "Error",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            else -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun CreateExercise(
    viewModel: AllExercisesViewModel,
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    var name by rememberSaveable { mutableStateOf("")}
    var muscleGroup by rememberSaveable { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { launcher.launch("image/*") },
        ) {
            Text("ФОТО")
        }
        imageUri?.let {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(it)
                    .size(Size.ORIGINAL)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = "Image",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        TextField(
            value = name,
            onValueChange = {name = it},
        )
        TextField(
            value = muscleGroup,
            onValueChange = {muscleGroup = it},
        )
        Button(
            onClick = {
                if(name.isNotBlank() && muscleGroup.isNotBlank()){
                    val imagePath = imageUri?.let { uri ->
                        copyUriToFile(context, uri)?.absolutePath
                    }
                    viewModel.addExercise(name,muscleGroup,imagePath)
                    name = ""
                    muscleGroup = ""
                    imageUri = null
                }
            },
        ) {
            Text("Создать")
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