package com.example.tiendazapatos.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendazapatos.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    var startZoomAnimation by remember { mutableStateOf(false) }
    var startFadeAnimation by remember { mutableStateOf(false) }


    val scale by animateFloatAsState(
        targetValue = if (startZoomAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 800), // Duración del zoom
        label = "scaleAnimation"
    )


    val alpha by animateFloatAsState(
        targetValue = if (startFadeAnimation) 0f else 1f,
        animationSpec = tween(durationMillis = 500),
        label = "alphaAnimation"
    )


    LaunchedEffect(key1 = true) {
        startZoomAnimation = true
        delay(1200)
        startFadeAnimation = true
        delay(500)
        navController.popBackStack()
        navController.navigate("inicio")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.zaretti),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .scale(scale)
                .alpha(alpha)
        )
    }
}
