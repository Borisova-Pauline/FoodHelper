package com.tomli.foodhelper.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tomli.foodhelper.R
import com.tomli.foodhelper.components.NavBarButtons
import com.tomli.foodhelper.components.NavigationBarButton
import com.tomli.foodhelper.database.FoodInfo
import com.tomli.foodhelper.database.User
import com.tomli.foodhelper.ui.theme.InterfaceGreen

@Composable
fun MainScreen(navController: NavController){
    val section= remember { mutableStateOf(NavBarButtons.AdditionalSections) }

    val profile = remember { mutableStateOf(User(0, "01.01.2001", "160", "70", "Мужской", "Умеренный")) }
    val foodDb = remember { mutableListOf<FoodInfo>(FoodInfo(0, "Тёмная энергия", "Идеально подходит для завтрака", 100F, 400F, 200F, 100F, 300F)) }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier= Modifier.padding(top=innerPadding.calculateTopPadding()).fillMaxSize()){
            Box(modifier= Modifier.fillMaxWidth().weight(1f)){
                when(section.value){
                    NavBarButtons.AdditionalSections->{

                    }
                    NavBarButtons.FoodDiary->{

                    }
                    NavBarButtons.FoodDB->{
                        FoodDbPage(foodDb)
                    }
                    NavBarButtons.Profile->{
                        ProfilePage(profile.value)
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