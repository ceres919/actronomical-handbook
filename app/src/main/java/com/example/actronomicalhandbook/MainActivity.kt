package com.example.actronomicalhandbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                MainNavRouter()
            }
        }
    }
}

@Composable
fun MainNavRouter() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "news") {
        composable("news") { NewsBanner(navController) }
        composable("opengl") { OpenGLScreen(navController) }
    }
}

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private var renderer : OpenGLRenderer
    init {
        setEGLContextClientVersion(2)
        renderer = OpenGLRenderer(context)
        setRenderer(renderer)
    }
    fun setSelectedPlanet(index: Int) {
        renderer.setSelectedPlanet(index)
    }
}

@Composable
fun OpenGLScreen(navController: NavController) {
    var selectedPlanetIndex by remember { mutableStateOf(0) }
    val planetCount = 9

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Transparent)

    ) {
        AndroidView(
            factory = { context ->
                MyGLSurfaceView(context).apply {
                    setSelectedPlanet(selectedPlanetIndex)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .padding(5.dp)
                .background(color = Color.Transparent)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    selectedPlanetIndex =
                        (selectedPlanetIndex - 1).takeIf { it >= 0 } ?: (planetCount - 1)
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 80.dp)
                    .padding(horizontal = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50))
            ) {
                Text("Влево")
            }

            Button(
                onClick = {
                    if (selectedPlanetIndex == 2) {
                        navController.navigate("moon_info")
                    }
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 80.dp)
                    .padding(horizontal = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50))
            ) {
                Text("Информация")
            }

            Button(
                onClick = {
                    selectedPlanetIndex = (selectedPlanetIndex + 1) % planetCount
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 80.dp)
                    .padding(horizontal = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50))
            ) {
                Text("Вправо")
            }
        }
    }
}