package com.tomli.foodhelper.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomli.foodhelper.R
import com.tomli.foodhelper.database.User

@Composable
fun ProfilePage(user: User){
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
    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        Text(text="Параметры пользователя", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp), fontSize = 22.sp)
        InfoField("Дата рождения:", isBirthdayChanging.value, {value->isBirthdayChanging.value=value}, birthday.value!!, birthday.value!!) {newText -> birthday.value=newText }
        Spacer(modifier = Modifier.height(20.dp))
        InfoField("Рост:", isHeightChanging.value, {value->isHeightChanging.value=value}, "${height.value} см.", height.value!!) {newText -> height.value=newText }
        Spacer(modifier = Modifier.height(20.dp))
        InfoField("Вес:", isWeightChanging.value, {value->isWeightChanging.value=value}, "${weight.value} кг.", weight.value!!) {newText -> weight.value=newText }
        Spacer(modifier = Modifier.height(20.dp))
        ChoosingField("Пол:", isGenderChanging.value, {value-> isGenderChanging.value=value}, gender.value!!, {newVal-> gender.value=newVal}, listOf("Мужской", "Женский"))
        Spacer(modifier = Modifier.height(20.dp))
        ChoosingField("Уровень активности:", isActivityLvChanging.value, {value-> isActivityLvChanging.value=value}, activityLv.value!!, {newVal-> activityLv.value=newVal},
            listOf(ActivityLevel.Low.screenName, ActivityLevel.Middle.screenName, ActivityLevel.High.screenName))
        Spacer(modifier = Modifier.height(20.dp))
    }
}



@Composable
fun InfoField(text: String, isChanging: Boolean, onChanging:(value: Boolean)->Unit, screenValue: String, changableVal: String, onValueChange:(newVal: String)->Unit){
    Row(verticalAlignment = Alignment.CenterVertically){
        Text(text= "$text ", color= Color(0xff707070))
        if(isChanging){
            OutlinedTextField(value = changableVal, onValueChange = onValueChange,
                trailingIcon = {
                    Image(painter = painterResource(R.drawable.ok_button), contentDescription = null, modifier = Modifier.size(25.dp)
                        .clickable { onChanging(false) })
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