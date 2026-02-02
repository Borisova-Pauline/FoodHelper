package com.tomli.foodhelper.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.foodhelper.R
import com.tomli.foodhelper.components.NavBarButtons
import com.tomli.foodhelper.components.NavigationBarButton
import com.tomli.foodhelper.database.FoodDiaryDB
import com.tomli.foodhelper.database.FoodDiaryList
import com.tomli.foodhelper.database.FoodInfo
import com.tomli.foodhelper.database.FoodVM
import com.tomli.foodhelper.database.User
import com.tomli.foodhelper.ui.theme.InterfaceGreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MainScreen(navController: NavController, foodVM: FoodVM = viewModel(factory = FoodVM.factory)){
    val section= remember { mutableStateOf(NavBarButtons.AdditionalSections) }

    val profile =remember { mutableStateOf(User(0, "01.01.2000", "160", "70", "Мужской", "Умеренный")) }
    foodVM.getUser { user-> profile.value=user }
    val foodDb = foodVM.allFoodDB.collectAsState(initial = emptyList())
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier= Modifier.padding(top=innerPadding.calculateTopPadding()).fillMaxSize()){
            Box(modifier= Modifier.fillMaxWidth().weight(1f)){
                when(section.value){
                    NavBarButtons.AdditionalSections->{
                        AdditionalScreen(navController)
                    }
                    NavBarButtons.FoodDiary->{
                        FoodDiary(profile.value, foodVM, foodDb)
                    }
                    NavBarButtons.FoodDB->{
                        FoodDbPage(foodDb.value, foodVM)
                    }
                    NavBarButtons.Profile->{
                        ProfilePage(profile.value, foodVM, {user-> profile.value=user})
                    }
                }
            }
            Row(modifier= Modifier.fillMaxWidth().wrapContentHeight().background(color= InterfaceGreen)){
                Box(modifier= Modifier.weight(1f).padding(5.dp), contentAlignment = Alignment.Center){
                    NavigationBarButton(NavBarButtons.AdditionalSections, section.value, R.drawable.add_section_image, {section.value=
                        NavBarButtons.AdditionalSections})
                }
                Box(modifier= Modifier.weight(1f).padding(5.dp), contentAlignment = Alignment.Center){
                    NavigationBarButton(
                        NavBarButtons.FoodDiary, section.value,
                        R.drawable.food_diary_image, {section.value= NavBarButtons.FoodDiary})
                }
                Box(modifier= Modifier.weight(1f).padding(5.dp), contentAlignment = Alignment.Center){
                    NavigationBarButton(
                        NavBarButtons.FoodDB, section.value,
                        R.drawable.food_db_image, {section.value= NavBarButtons.FoodDB})
                }
                Box(modifier= Modifier.weight(1f).padding(5.dp), contentAlignment = Alignment.Center){
                    NavigationBarButton(
                        NavBarButtons.Profile, section.value,
                        R.drawable.user_image, {section.value= NavBarButtons.Profile})
                }
            }
            Spacer(modifier= Modifier.fillMaxWidth().height(innerPadding.calculateBottomPadding()).background(color= InterfaceGreen))
        }
    }
}