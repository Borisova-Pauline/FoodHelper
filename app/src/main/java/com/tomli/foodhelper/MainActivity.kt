package com.tomli.foodhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomli.foodhelper.ui.theme.FoodHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodHelperTheme {

            }
        }
    }
}


@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ""
    ) {
        composable("main_screen") {
            MainScreen(navController)
        }
    }
}


@Composable
fun MainScreen(navController: NavController){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier=Modifier.padding(innerPadding).fillMaxSize()){
            Box(modifier=Modifier.fillMaxWidth().weight(1f)){

            }
            Row(modifier=Modifier.fillMaxWidth().wrapContentHeight()){

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodHelperTheme {

    }
}