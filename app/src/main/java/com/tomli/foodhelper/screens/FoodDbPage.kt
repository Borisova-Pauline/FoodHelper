package com.tomli.foodhelper.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tomli.foodhelper.database.FoodInfo
import com.tomli.foodhelper.database.FoodVM
import com.tomli.foodhelper.ui.theme.ChosenGreen
import com.tomli.foodhelper.ui.theme.OnChosenGreen
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodDbPage(foodDb: List<FoodInfo>, foodVM: FoodVM){
    val lookFood = remember { mutableStateOf(FoodInfo(-1, "", "", 100F, 400F, 200F, 100F, 300F)) }
    val isMoreActive = remember { mutableStateOf(false) }
    val isCreate = remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val isDelete = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text="База продуктов", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp), fontSize = 22.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text="+", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp)
                .clickable { isCreate.value=true }, fontSize = 24.sp)
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(foodDb, key={item -> item.id!!}){item->
                val dropDownState = remember { mutableStateOf(false) }
                Card(colors = CardDefaults.cardColors(
                    containerColor = ChosenGreen
                ), border = BorderStroke(2.dp, OnChosenGreen),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).combinedClickable(onClick = {lookFood.value=item; isMoreActive.value=true},
                        onLongClick = {lookFood.value=item; dropDownState.value=true}, onDoubleClick = {})) {
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)){
                        Text(text=item.name ?: "")
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text="${item.calories} ККал/${item.grams} гр.")
                    }
                    DropdownMenu(expanded = dropDownState.value, onDismissRequest = {dropDownState.value=false}) {
                        DropdownMenuItem(text={ Text(text="Редактировать")},
                            onClick = {isUpdate.value=true; dropDownState.value=false})
                        DropdownMenuItem(text={ Text(text="Удалить")},
                            onClick = {isDelete.value=true; dropDownState.value=false})
                    }
                }
            }
        }
    }
    if(isMoreActive.value){
        WindowFoodDbMore(lookFood.value) { isMoreActive.value=false }
    }
    if(isCreate.value){
        FoodActions(true, FoodInfo(null, null, null, null, null, null, null, null),
            {isCreate.value=false}, {food->foodVM.addFoodToDB(food) })
    }
    if(isUpdate.value){
        FoodActions(false, lookFood.value, {isUpdate.value=false}) {food-> foodVM.updateFoodInDB(food) }
    }
    if(isDelete.value){
        AlertDialog(onDismissRequest = {isDelete.value=false},
            title = {Text(text="Удалить продукт?")},
            confirmButton = {Text(text = "Удалить", modifier = Modifier.padding(5.dp)
                .clickable {
                    foodVM.deleteFoodInDB(lookFood.value)
                    isDelete.value=false})},
            dismissButton = {Text(text = "Отменить", modifier = Modifier.padding(5.dp)
                .clickable { isDelete.value=false})})
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


@Composable
fun FoodActions(isCreating: Boolean,food: FoodInfo, onDismiss:()->Unit, create:(food: FoodInfo)->Unit){
    val name = remember { mutableStateOf(food.name ?: "") }
    val description = remember { mutableStateOf(food.description ?: "") }
    val grams = remember { mutableStateOf(food.grams?.toString() ?: "100") }
    val calories = remember { mutableStateOf(food.calories?.toString() ?: "") }
    val proteins = remember { mutableStateOf(food.proteins?.toString() ?: "") }
    val fats = remember { mutableStateOf(food.fats?.toString() ?: "") }
    val carbohydrates = remember { mutableStateOf(food.carbohydrates?.toString() ?: "") }

    val context = LocalContext.current
    Dialog(onDismiss) {
        Card(modifier=Modifier.height(400.dp)){
            Column(modifier=Modifier.padding(10.dp)){
                Text(text="Добавление нового продукта", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier=Modifier.verticalScroll(rememberScrollState())) {
                    InputSimple("Название", false, value = name.value) {newVal-> name.value=newVal}
                    InputSimple("Описание (не обязательно)", false, value = description.value) {newVal-> description.value=newVal}
                    InputSimple("Граммы", true, value = grams.value) {newVal-> grams.value=newVal}
                    InputSimple("ККал", true, value = calories.value) {newVal-> calories.value=newVal}
                    InputSimple("Белки", true, value = proteins.value) {newVal-> proteins.value=newVal}
                    InputSimple("Жиры", true, value = fats.value) {newVal-> fats.value=newVal}
                    InputSimple("Углеводы", true, value = carbohydrates.value) {newVal-> carbohydrates.value=newVal}
                    Spacer(modifier = Modifier.height(10.dp))
                    Row{
                        Card(modifier = Modifier.weight(1f), border = BorderStroke(1.dp, OnChosenGreen),
                            onClick = {onDismiss()}) {
                            Text(text="Отмена", modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), textAlign = TextAlign.Center)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Card(modifier = Modifier.weight(1f), border = BorderStroke(1.dp, OnChosenGreen),
                            onClick = {
                                if(name.value!=""){
                                    try{
                                        if(grams.value.toFloat()>0F && calories.value.toFloat()>0F && proteins.value.toFloat()>0F
                                            && fats.value.toFloat()>0F && carbohydrates.value.toFloat()>0F){
                                            create(FoodInfo(food.id, name.value, description.value, grams.value.toFloat(), calories.value.toFloat(), proteins.value.toFloat(), fats.value.toFloat(), carbohydrates.value.toFloat())); onDismiss()
                                        }else{
                                            Toast.makeText(context, "Убедитесь, что значения граммов и КБЖУ больше нуля", Toast.LENGTH_LONG).show()
                                        }
                                    }catch (e: Exception){
                                        Toast.makeText(context, "Неправильный ввод", Toast.LENGTH_LONG).show()
                                    }
                                }else{
                                    Toast.makeText(context, "Введите название", Toast.LENGTH_LONG).show()
                                }
                            }) {
                            Text(text= if(isCreating) "Добавить" else "Обновить", modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun InputSimple(label: String, isNumberKeys: Boolean=false, value: String, onValChange:(newText: String)->Unit){
    OutlinedTextField(value=value, onValueChange=onValChange,
        keyboardOptions = if(isNumberKeys)KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        label = {Text(text=label)},
        modifier = Modifier.fillMaxWidth().padding(3.dp)
    )
}
