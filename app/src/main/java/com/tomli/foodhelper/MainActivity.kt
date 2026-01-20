package com.tomli.foodhelper

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomli.foodhelper.screens.HelloPage
import com.tomli.foodhelper.screens.MainScreen
import com.tomli.foodhelper.ui.theme.FoodHelperTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodHelperTheme {
                val context = LocalContext.current
                val sharedPrefs = context.getSharedPreferences("this_prefs", MODE_PRIVATE)
                val isHaveUser=remember{ mutableStateOf(sharedPrefs.getBoolean("isHaveUser", false))}
                Navigation(isHaveUser.value)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Navigation(isHaveUser: Boolean){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if(isHaveUser)"main_screen" else "HelloPage"
    ) {
        composable("HelloPage"){
            HelloPage(navController)
        }
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