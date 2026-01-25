package com.tomli.foodhelper.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.foodhelper.R
import com.tomli.foodhelper.database.FoodVM
import com.tomli.foodhelper.ui.theme.InterfaceGreen

@Composable
fun StatisticScreen(navController: NavController, foodVM: FoodVM= viewModel(factory = FoodVM.factory)){
    val foodDiary = foodVM.allFoodDiary.collectAsState(initial = emptyList())
    val caloriesState = rememberLazyListState()
    val proteinsState = rememberLazyListState()
    val fatsState = rememberLazyListState()
    val carbohydratesState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (foodDiary.value.isNotEmpty()) {
            caloriesState.scrollToItem(foodDiary.value.size - 1)
            proteinsState.scrollToItem(foodDiary.value.size - 1)
            fatsState.scrollToItem(foodDiary.value.size - 1)
            carbohydratesState.scrollToItem(foodDiary.value.size - 1)
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier= Modifier.padding(innerPadding).padding(horizontal = 10.dp).fillMaxSize()) {
            Row{
                Image(painter = painterResource(R.drawable.arrow_down), contentDescription = null,
                    modifier = Modifier.rotate(90F).size(30.dp).clickable { navController.navigate("main_screen") })
                Spacer(modifier=Modifier.width(20.dp))
                Text(text="Статистика", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier=Modifier.height(20.dp))
            Column(modifier=Modifier.verticalScroll(rememberScrollState())){
                Text(text="Калории")
                Box(modifier=Modifier.fillMaxWidth().border(1.dp, color= InterfaceGreen)){
                    LazyRow(state = caloriesState) {
                        items(foodDiary.value){ item->
                            ProgressCPFCone(modifier=Modifier.width(100.dp), item.haveCalories!!, item.needCalories!!, item.date!!)
                        }
                    }
                }
                Spacer(modifier=Modifier.height(20.dp))
                Text(text="Белки")
                Box(modifier=Modifier.fillMaxWidth().border(1.dp, color= InterfaceGreen)){
                    LazyRow(state = proteinsState) {
                        items(foodDiary.value){ item->
                            ProgressCPFCone(modifier=Modifier.width(100.dp), item.haveProteins!!, item.needProteins!!, item.date!!)
                        }
                    }
                }
                Spacer(modifier=Modifier.height(20.dp))
                Text(text="Жиры")
                Box(modifier=Modifier.fillMaxWidth().border(1.dp, color= InterfaceGreen)){
                    LazyRow(state = fatsState) {
                        items(foodDiary.value){ item->
                            ProgressCPFCone(modifier=Modifier.width(100.dp), item.haveFats!!, item.needFats!!, item.date!!)
                        }
                    }
                }
                Spacer(modifier=Modifier.height(20.dp))
                Text(text="Углеводы")
                Box(modifier=Modifier.fillMaxWidth().border(1.dp, color= InterfaceGreen)){
                    LazyRow(state = carbohydratesState) {
                        items(foodDiary.value){ item->
                            ProgressCPFCone(modifier=Modifier.width(100.dp), item.haveCarbohydrates!!, item.needCarbohydrates!!, item.date!!)
                        }
                    }
                }
            }
        }
    }
}