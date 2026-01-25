package com.tomli.foodhelper.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tomli.foodhelper.R
import com.tomli.foodhelper.database.FoodDB
import com.tomli.foodhelper.database.FoodDiaryDB
import com.tomli.foodhelper.database.FoodDiaryList
import com.tomli.foodhelper.database.FoodDiaryOne
import com.tomli.foodhelper.database.FoodInfo
import com.tomli.foodhelper.database.FoodVM
import com.tomli.foodhelper.database.User
import com.tomli.foodhelper.ui.theme.ChosenGreen
import com.tomli.foodhelper.ui.theme.InterfaceGreen
import com.tomli.foodhelper.ui.theme.OnChosenGreen
import com.tomli.foodhelper.ui.theme.ProgressBackground
import com.tomli.foodhelper.ui.theme.ProgressFill
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodDiary(user: User, foodVM: FoodVM, foodDB: State<List<FoodInfo>>){
    val CPFC = remember { mutableStateOf(calculateCPFC(user))}
    val date = remember { mutableStateOf(formatNowDateToString()) }
    val foodDiaryOne = remember { mutableStateOf<FoodDiaryDB?>(FoodDiaryDB(null, date.value, 0F, 0F, 0F, 0F, 0F, 0F,
        0F, 0F, Json.encodeToString(FoodDiaryList(mutableListOf())))) }
    val diaryList = remember { mutableStateOf(FoodDiaryList(mutableListOf())) }
    foodVM.getLastFoodDiaryDay(date.value, {foodDiary-> foodDiaryOne.value=foodDiary}, {list-> diaryList.value=list})
    foodDiaryOne.value = foodDiaryOne.value?.copy(needCalories = CPFC.value.calories, needProteins = CPFC.value.proteins,
        needFats = CPFC.value.fats, needCarbohydrates = CPFC.value.carbohydrates)
    val lambdaCPFC_Doing={ compareCPFC(diaryList.value, {cal, pro, fat, car->foodDiaryOne.value = foodDiaryOne.value?.copy(
        haveCalories = cal, haveProteins = pro, haveFats = fat, haveCarbohydrates = car
    )})}
    lambdaCPFC_Doing()

    val isAdding = remember { mutableStateOf(false) }
    val isDeleting = remember { mutableStateOf(false) }
    val deletingIndex = remember { mutableStateOf(-1) }
    Column(modifier= Modifier.padding(10.dp)) {
        Text(text="Сегодня, ${date.value}", modifier=Modifier.padding(bottom = 5.dp).fillMaxWidth(),
            fontSize = 22.sp, textAlign = TextAlign.Center)
        Row(modifier=Modifier.fillMaxWidth()){
            ProgressCPFCone(modifier=Modifier.weight(1f), foodDiaryOne.value?.haveCalories!!, foodDiaryOne.value?.needCalories!!, "ККал")
            ProgressCPFCone(modifier=Modifier.weight(1f), foodDiaryOne.value?.haveProteins!!, foodDiaryOne.value?.needProteins!!, "Белки")
            ProgressCPFCone(modifier=Modifier.weight(1f), foodDiaryOne.value?.haveFats!!, foodDiaryOne.value?.needFats!!, "Жиры")
            ProgressCPFCone(modifier=Modifier.weight(1f), foodDiaryOne.value?.haveCarbohydrates!!, foodDiaryOne.value?.needCarbohydrates!!, "Углеводы")
        }
        Spacer(modifier=Modifier.height(10.dp))
        Card(modifier=Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = ChosenGreen),
            border = BorderStroke(1.dp, InterfaceGreen)){
            Row(modifier = Modifier.padding(10.dp)){
                Text(text="Дневник питания", color= OnChosenGreen)
                Spacer(modifier=Modifier.weight(1f))
                Text(text="Добавить", color= OnChosenGreen, modifier=Modifier.clickable { isAdding.value=true })
            }
        }
        Spacer(modifier=Modifier.height(3.dp))
        Card(modifier= Modifier.fillMaxWidth().height(520.dp), colors = CardDefaults.cardColors(containerColor = ChosenGreen),
            border = BorderStroke(1.dp, InterfaceGreen)){
            LazyColumn(modifier=Modifier.padding(5.dp).fillMaxWidth()) {
                items(diaryList.value.list.size){index->
                    val dropDownState = remember { mutableStateOf(false) }
                    Card(modifier= Modifier.fillMaxWidth().padding(vertical = 3.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, InterfaceGreen)){
                        Column(modifier= Modifier.padding(vertical=3.dp, horizontal = 5.dp).
                        combinedClickable(onClick = {}, onLongClick = {deletingIndex.value=index; dropDownState.value=true}, onDoubleClick = {})){
                            Row{
                                Text(text=diaryList.value.list[index].time, color= ProgressFill, fontWeight = FontWeight.Bold)
                                Spacer(modifier=Modifier.width(5.dp))
                                Text(text="${diaryList.value.list[index].food.name}", modifier=Modifier.weight(1f))
                                Spacer(modifier=Modifier.width(5.dp))
                                Text(text="${diaryList.value.list[index].food.grams} гр.")
                            }
                            Row{
                                Text(text="${"%.2f".format(diaryList.value.list[index].food.calories)} К.", modifier=Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 15.sp)
                                Text(text="${"%.2f".format(diaryList.value.list[index].food.proteins)} Б.", modifier=Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 15.sp)
                                Text(text="${"%.2f".format(diaryList.value.list[index].food.fats)} Ж.", modifier=Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 15.sp)
                                Text(text="${"%.2f".format(diaryList.value.list[index].food.carbohydrates)} У.", modifier=Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 15.sp)
                            }
                        }
                        DropdownMenu(expanded = dropDownState.value, onDismissRequest = {dropDownState.value=false}) {
                            DropdownMenuItem(text={ Text(text="Удалить")},
                                onClick = {isDeleting.value=true; dropDownState.value=false})
                        }
                    }
                }
            }
        }
    }
    if(isAdding.value){
        AddFoodDiaryDialog({isAdding.value=false}, {food-> diaryList.value.list.add(food); lambdaCPFC_Doing()
            foodVM.updateFoodDiaryToday(foodDiaryOne.value!!, diaryList.value)
            foodVM.getLastFoodDiaryDay(date.value, {foodDiary-> foodDiaryOne.value=foodDiary}, {list-> diaryList.value=list})}, foodDB)
    }
    if(isDeleting.value){
        AlertDialog(onDismissRequest = {isDeleting.value=false},
            title = {Text(text="Удалить продукт?")},
            confirmButton = {Text(text = "Удалить", modifier = Modifier.padding(5.dp)
                .clickable {
                    diaryList.value.list.removeAt(deletingIndex.value)
                    lambdaCPFC_Doing()
                    foodVM.updateFoodDiaryToday(foodDiaryOne.value!!, diaryList.value)
                    isDeleting.value=false
                    foodVM.getLastFoodDiaryDay(date.value, {foodDiary-> foodDiaryOne.value=foodDiary}, {list-> diaryList.value=list})})},
            dismissButton = {Text(text = "Отменить", modifier = Modifier.padding(5.dp)
                .clickable { isDeleting.value=false})})
    }
}

@Composable
fun ProgressCPFCone(modifier: Modifier, current: Float, max: Float, name: String){
    Column(modifier=modifier, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text="${"%.2f".format(current)}/${"%.2f".format(max)}", fontSize = 12.sp)
        VerticalProgress(current, max, 30.dp, 150F)
        Text(text=name)
    }
}

@Composable
fun VerticalProgress(current: Float, max: Float, width: Dp, height: Float){
    val filledProgress=if(current<max){
        current/max*height
    }else{ height }
    Box(modifier = Modifier.width(width).height(height.toInt().dp).background(color = ProgressBackground), contentAlignment = Alignment.Center){
        Spacer(modifier = Modifier.fillMaxWidth().height(filledProgress.toInt().dp).background(color= ProgressFill).align(Alignment.BottomCenter))
    }
}

@Composable
fun AddFoodDiaryDialog(onDismiss:()->Unit, onOk:(foodOne: FoodDiaryOne)->Unit, foodDB: State<List<FoodInfo>>){
    val chosenFood = remember { mutableStateOf<FoodInfo?>(null) }
    val isChooseFood = remember { mutableStateOf(false) }
    val time = LocalTime.now()
    val timeString = "${time.hour}:${time.minute}"
    val grams = remember { mutableStateOf("100") }
    val context = LocalContext.current
    Dialog(onDismiss) {
        Card{
            Column(modifier=Modifier.padding(10.dp)){
                if(chosenFood.value==null){
                    Card(colors = CardDefaults.cardColors(containerColor = ChosenGreen),
                        border = BorderStroke(1.dp, InterfaceGreen), onClick = {isChooseFood.value=true}){
                        Text(text="Выбрать продукт", modifier = Modifier.padding(5.dp))
                    }
                }else{
                    Card(colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, OnChosenGreen)){
                        Row(modifier = Modifier.padding(5.dp)){
                            Text(text="${chosenFood.value?.name}")
                            Spacer(modifier=Modifier.weight(1f))
                            Image(painter = painterResource(R.drawable.refresh_buttton), contentDescription = null,
                                modifier=Modifier.size(30.dp).clickable { isChooseFood.value=true })
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text="Время: $timeString")
                Spacer(modifier = Modifier.padding(10.dp))
                InputSimple("Граммы", true, value = grams.value) {newVal-> grams.value=newVal}
                Spacer(modifier = Modifier.padding(10.dp))
                Row{
                    Card(modifier = Modifier.weight(1f), border = BorderStroke(1.dp, OnChosenGreen),
                        onClick = {onDismiss()}) {
                        Text(text="Отмена", modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Card(modifier = Modifier.weight(1f), border = BorderStroke(1.dp, OnChosenGreen),
                        onClick = {
                            try{
                                onOk(getCPFCforGrams(timeString, grams.value.toFloat(), chosenFood.value!!))
                                onDismiss()
                            }catch (e: Exception){
                                Toast.makeText(context, "Убедитесь в правильности ввода", Toast.LENGTH_LONG).show()
                            }
                        }) {
                        Text(text= "Добавить", modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), textAlign = TextAlign.Center)
                    }
                }
            }
            if(isChooseFood.value){
                Dialog({isChooseFood.value=false}) {
                    Card(modifier=Modifier.heightIn(max = 400.dp)){
                        LazyColumn(modifier=Modifier.padding(5.dp)) {
                            items(foodDB.value, key={item -> item.id!!}){item->
                                Card(colors = CardDefaults.cardColors(
                                    containerColor = ChosenGreen
                                ), border = BorderStroke(2.dp, OnChosenGreen),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                                        .clickable { chosenFood.value=item; isChooseFood.value=false }) {
                                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)){
                                        Text(text=item.name ?: "")
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(text="${item.calories} ККал/${item.grams} гр.")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getCPFCforGrams(time: String,grams: Float, food: FoodInfo): FoodDiaryOne{
    val ratio = grams/food.grams!!
    return FoodDiaryOne(time, FoodInfo(null, food.name, null, grams, ratio*food.calories!!,
        ratio*food.proteins!!, ratio*food.fats!!, ratio*food.carbohydrates!!))
}

data class CPFC( //КБЖУ
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbohydrates: Float
)

fun calculateCPFC(user: User): CPFC{
    val years = calculateAge(user.birthday!!)
    val BRM = if(user.gender=="Мужской"){
        (10F * user.weight!!.toFloat()) + (6.25F * user.height!!.toFloat()) - (5F * years.toFloat()) + 5F
    }else{
        (10F * user.weight!!.toFloat()) + (6.25F * user.height!!.toFloat()) - (5F * years.toFloat()) -161F
    }
    val activ: Float = when(user.activityLv){
        ActivityLevel.Low.screenName -> 1.375F
        ActivityLevel.Middle.screenName -> 1.55F
        ActivityLevel.High.screenName -> 1.725F
        else -> 1.55F
    }
    val calories = BRM * activ
    val proteins = (calories * 0.30F)/4F
    val fats = (calories * 0.30F)/9F
    val carbohydrates = (calories * 0.40F)/4F
    return CPFC(calories, proteins, fats, carbohydrates)
}

fun calculateAge(date: String): Int{
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val dateC: LocalDate = try{
        LocalDate.parse(date, dateFormatter)
    }catch (e: Exception){
        LocalDate.now()
    }
    return ChronoUnit.YEARS.between(dateC, LocalDate.now()).toInt()
}


/*fun isItThisDay(date: String?): Boolean{
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val localDate = LocalDate.now()
    val thisDay = localDate.format(dateFormatter)
    return date==thisDay
}*/

fun formatNowDateToString(): String{
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val localDate = LocalDate.now()
    return localDate.format(dateFormatter)
}

fun compareCPFC(list: FoodDiaryList, onReturn:(calories: Float, proteins: Float, fats: Float, carbohydrates: Float)->Unit){
    var calories = 0F
    var proteins = 0F
    var fats = 0F
    var carbohydrates = 0F
    list.list.forEach{item->
        calories+=item.food.calories ?: 0F
        proteins+=item.food.proteins ?: 0F
        fats+=item.food.fats ?: 0F
        carbohydrates+=item.food.carbohydrates ?: 0F
    }
    onReturn(calories, proteins, fats, carbohydrates)
}