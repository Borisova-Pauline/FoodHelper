package com.tomli.foodhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomli.foodhelper.screens.MainScreen
import com.tomli.foodhelper.ui.theme.FoodHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodHelperTheme {
                Navigation()
            }
        }
    }
}


@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable("main_screen") {
            MainScreen(navController)
        }
    }
}







/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodHelperTheme {

    }
}*/