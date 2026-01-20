package com.tomli.foodhelper.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tomli.foodhelper.database.FoodInfo
import com.tomli.foodhelper.ui.theme.ChosenGreen
import com.tomli.foodhelper.ui.theme.OnChosenGreen

@Composable
fun FoodDbPage(foodDb: List<FoodInfo>){
    val lookFood = remember { mutableStateOf(FoodInfo(-1, "", "", 100F, 400F, 200F, 100F, 300F)) }
    val isMoreActive = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text="База продуктов", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp), fontSize = 22.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text="+", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp), fontSize = 24.sp)
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(foodDb, key={item -> item.id!!}){item->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                    containerColor = ChosenGreen
                ), border = BorderStroke(2.dp, OnChosenGreen), onClick = {lookFood.value=item; isMoreActive.value=true}) {
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)){
                        Text(text=item.name ?: "")
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text="${item.calories} ККал/${item.grams} гр.")
                    }
                }
            }
        }
    }
    if(isMoreActive.value){
        WindowFoodDbMore(lookFood.value) { isMoreActive.value=false }
    }
}


@Composable
fun WindowFoodDbMore(item: FoodInfo, onDismiss:()->Unit){
    Dialog(onDismiss) {
        Card{
            Column(modifier=Modifier.padding(10.dp).verticalScroll(rememberScrollState())){
                Text(text=item.name!!, modifier=Modifier.fillMaxWidth().padding(bottom = 5.dp), textAlign = TextAlign.Center,
                    fontSize = 20.sp, fontWeight = FontWeight.Bold)
                if(item.description!=null && item.description!=""){
                    Text(text=item.description)
                }
                Spacer(modifier=Modifier.height(20.dp))
                Text(text="${item.calories} ККал")
                Text(text="${item.proteins} белков")
                Text(text="${item.fats} жиров")
                Text(text="${item.carbohydrates} углеводов")
            }
        }
    }
}