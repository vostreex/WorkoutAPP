package com.example.presentation.Screens

import android.R
import android.text.Layout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.domain.model.Exercise
import java.io.File

@Composable
fun ShowExerciseCard(
    modifier: Modifier = Modifier,
    exercise: Exercise
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )
    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                )
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
                .alpha(animatedAlpha)
                .scale(animatedScale),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.size(16.dp))
                }

                exercise.gifURL?.let { url ->
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            GifImage(
                               url
                            )
                        }
                        Spacer(Modifier.size(16.dp))
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            text = "Группа мышц: ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = exercise.muscleGroup,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(16.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.size(8.dp))
                                Text(
                                    text = "Описание:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                            Spacer(Modifier.size(8.dp))
                            exercise.aboutExercise?.let { description ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 8.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    shadowElevation = 4.dp,
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = description,
                                        modifier = Modifier.padding(12.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Justify,
                                        lineHeight = 24.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } ?: Text(
                                text = "Описание отсутствует",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GifImage(
    data: String,
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
    Box(modifier = Modifier) {
        when {
            hasError.value -> {
                Image(
                    painter = painterResource(id = errorResId),
                    contentDescription = contentDescription ?: "Error",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )
            }
            else -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}