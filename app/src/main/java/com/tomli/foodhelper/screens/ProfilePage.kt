package com.tomli.foodhelper.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomli.foodhelper.R
import com.tomli.foodhelper.database.FoodVM
import com.tomli.foodhelper.database.User
import com.tomli.foodhelper.ui.theme.OnChosenGreen
import com.tomli.foodhelper.ui.theme.ProgressFill
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun ProfilePage(user: User, foodVM: FoodVM, updateLocalUser:(user:User)->Unit){
    val birthday = remember { mutableStateOf(user.birthday) }
    val isBirthdayChanging = remember { mutableStateOf(false) }
    val height = remember { mutableStateOf(user.height) }
    val isHeightChanging = remember { mutableStateOf(false) }
    val weight = remember { mutableStateOf(user.weight) }
    val isWeightChanging = remember { mutableStateOf(false) }
    val gender = remember { mutableStateOf(user.gender) }
    val isGenderChanging = remember { mutableStateOf(false) }
    val activityLv = remember { mutableStateOf(user.activityLv) }
    val isActivityLvChanging = remember { mutableStateOf(false) }

    val saveChangingsLambda = {val user = User(user.id, birthday.value, height.value, weight.value, gender.value, activityLv.value)
        foodVM.updateUser(user); updateLocalUser(user)}
    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        Text(text="Параметры пользователя", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp), fontSize = 22.sp)
        //InfoField("Дата рождения:", isBirthdayChanging.value, {value->isBirthdayChanging.value=value}, birthday.value!!, birthday.value!!) {newText -> birthday.value=newText }
        DateInput("Дата рождения:", isBirthdayChanging.value, {value->isBirthdayChanging.value=value}, birthday.value!!) {newText -> birthday.value=newText; saveChangingsLambda() }
        Spacer(modifier = Modifier.height(20.dp))
        InfoField("Рост:", isHeightChanging.value, {value->isHeightChanging.value=value}, "${height.value} см.", height.value!!) {newText -> height.value=newText; saveChangingsLambda() }
        Spacer(modifier = Modifier.height(20.dp))
        InfoField("Вес:", isWeightChanging.value, {value->isWeightChanging.value=value}, "${weight.value} кг.", weight.value!!) {newText -> weight.value=newText; saveChangingsLambda() }
        Spacer(modifier = Modifier.height(20.dp))
        ChoosingField("Пол:", isGenderChanging.value, {value-> isGenderChanging.value=value}, gender.value!!, {newVal-> gender.value=newVal; saveChangingsLambda()}, listOf("Мужской", "Женский"))
        Spacer(modifier = Modifier.height(20.dp))
        ChoosingField("Уровень активности:", isActivityLvChanging.value, {value-> isActivityLvChanging.value=value}, activityLv.value!!, {newVal-> activityLv.value=newVal; saveChangingsLambda()},
            listOf(ActivityLevel.Low.screenName, ActivityLevel.Middle.screenName, ActivityLevel.High.screenName))
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(text: String, isChanging: Boolean, onChanging:(value: Boolean)->Unit, screenValue: String, onValueChange:(newVal: String)->Unit){
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val state = rememberDatePickerState(initialSelectedDateMillis =selectedDate.value.toEpochDay())
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically){
        Text(text= "$text ", color= Color(0xff707070))
        if(isChanging){
            Row{
                Text(text="Выбор даты")
                Spacer(modifier = Modifier.width(40.dp))
                CircularProgressIndicator(modifier = Modifier.size(25.dp), color = ProgressFill)
            }
            DatePickerDialog(
                onDismissRequest = { onChanging(false) },
                confirmButton = {
                    Text("Выбрать", modifier = Modifier.padding(start=40.dp, end=10.dp).clickable{
                        if(isLocalDateAfterMillis(state.selectedDateMillis!!, LocalDate.now())){
                            onValueChange(convertMillisToDate(state.selectedDateMillis!!));onChanging(false)
                        }else{
                            Toast.makeText(context, "Выберите дату раньше текущей", Toast.LENGTH_LONG).show()
                        }})
                },
                dismissButton = {
                    Text("Отмена", modifier = Modifier.clickable{onChanging(false)})
                }
            ) {
                DatePicker(
                    state = state
                )
            }
        }else{
            Text(text=screenValue)
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(R.drawable.button_change), contentDescription = null, modifier = Modifier.padding(start = 10.dp).size(25.dp)
                .clickable { onChanging(true) })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun isLocalDateAfterMillis(timeStamp: Long, date: LocalDate): Boolean{
    val instant = Instant.ofEpochMilli(timeStamp)
    val localDateTime = LocalDate.ofInstant(instant, ZoneId.systemDefault())
    return date.isAfter(localDateTime)
}

fun convertMillisToDate(timeStamp: Long): String{
    val instant = Instant.ofEpochMilli(timeStamp)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return localDateTime.format(formatter)
}

@Composable
fun InfoField(text: String, isChanging: Boolean, onChanging:(value: Boolean)->Unit, screenValue: String, changableVal: String, onValueChange:(newVal: String)->Unit){
    val tempVal = remember { mutableStateOf(changableVal) }
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically){
        Text(text= "$text ", color= Color(0xff707070))
        if(isChanging){
            OutlinedTextField(value = tempVal.value, onValueChange = {newVal-> tempVal.value=newVal},
                trailingIcon = {
                    Image(painter = painterResource(R.drawable.ok_button), contentDescription = null, modifier = Modifier.size(25.dp)
                        .clickable { try{
                            if(tempVal.value!="" && tempVal.value.toFloat()>0F){
                                onValueChange(tempVal.value)
                                onChanging(false)
                            }
                        }catch (e: Exception){
                            Toast.makeText(context, "Убедитесь, что значение введено правильно", Toast.LENGTH_LONG).show()
                        }})
                },
                modifier = Modifier.padding(start = 7.dp).weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }else{
            Text(text=screenValue)
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(R.drawable.button_change), contentDescription = null, modifier = Modifier.padding(start = 10.dp).size(25.dp)
                .clickable { onChanging(true) })
        }
    }
}

@Composable
fun ChoosingField(text: String, isChanging: Boolean, onChanging:(value: Boolean)->Unit, value: String, onValueChange:(newVal: String)->Unit, variants: List<String>){
    val dropDown = remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically){
        Text(text= "$text ", color= Color(0xff707070))
        if(isChanging){
            OutlinedTextField(value = value, onValueChange = onValueChange,
                trailingIcon = {
                    Row{
                        Image(painter = painterResource(if(dropDown.value) R.drawable.arrow_up else R.drawable.arrow_down), contentDescription = null, modifier = Modifier.padding(7.dp).size(25.dp)
                            .clickable { dropDown.value=!dropDown.value })
                        Image(painter = painterResource(R.drawable.ok_button), contentDescription = null, modifier = Modifier.padding(7.dp).size(25.dp)
                            .clickable { onChanging(false) })
                    }
                },
                modifier = Modifier.padding(start = 7.dp).weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                readOnly = true
            )
        }else{
            Text(text=value)
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(R.drawable.button_change), contentDescription = null, modifier = Modifier.padding(start = 10.dp).size(25.dp)
                .clickable { onChanging(true) })
        }
        DropdownMenu(expanded = dropDown.value, onDismissRequest = {dropDown.value=false}) {
            variants.forEach{ item->
                DropdownMenuItem(
                    text={Text(text=item)},
                    onClick = {onValueChange(item); dropDown.value=false}
                )
            }
        }
    }
}

enum class ActivityLevel(val screenName: String){
    Low("Низкий"), Middle("Умеренный"), High("Высокий")
}