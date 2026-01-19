package com.tomli.foodhelper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomli.foodhelper.database.User
import com.tomli.foodhelper.ui.theme.ProgressBackground
import com.tomli.foodhelper.ui.theme.ProgressFill
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun FoodDiary(user: User){
    val tempVal = remember { mutableStateOf(CPFC(10F, 10F, 10F, 10F)) }
    val CPFC = remember { mutableStateOf(calculateCPFC(user))}
    val date = LocalDate.now()
    Column(modifier= Modifier.padding(10.dp)) {
        Text(text="Сегодня, ${date.dayOfMonth}.${date.month.value}.${date.year}", modifier=Modifier.padding(bottom = 5.dp).fillMaxWidth(),
            fontSize = 22.sp, textAlign = TextAlign.Center)
        Row(modifier=Modifier.fillMaxWidth()){
            ProgressCPFCone(modifier=Modifier.weight(1f), tempVal.value.calories, CPFC.value.calories, "ККал")
            ProgressCPFCone(modifier=Modifier.weight(1f), tempVal.value.proteins, CPFC.value.proteins, "Белки")
            ProgressCPFCone(modifier=Modifier.weight(1f), tempVal.value.fats, CPFC.value.fats, "Жиры")
            ProgressCPFCone(modifier=Modifier.weight(1f), tempVal.value.carbohydrates, CPFC.value.carbohydrates, "Углеводы")
        }
    }
}

@Composable
fun ProgressCPFCone(modifier: Modifier, current: Float, max: Float, name: String){
    Column(modifier=modifier, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text="${current}/${(max*100).roundToInt()/100F}", fontSize = 12.sp)
        VerticalProgress(current, max, 30.dp, 150F)
        Text(text=name)
    }
}

@Composable
fun VerticalProgress(current: Float, max: Float, width: Dp, height: Float){
    val filledProgress=if(current<max){
        current/max*150F
    }else{ 1F }
    Box(modifier = Modifier.width(width).height(height.toInt().dp).background(color = ProgressBackground), contentAlignment = Alignment.Center){
        Spacer(modifier = Modifier.fillMaxWidth().height(filledProgress.toInt().dp).background(color= ProgressFill).align(Alignment.BottomCenter))
    }
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