package com.tomli.foodhelper.screens

import android.app.Activity.MODE_PRIVATE
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.foodhelper.R
import com.tomli.foodhelper.database.FoodVM
import com.tomli.foodhelper.ui.theme.InterfaceGreen
import com.tomli.foodhelper.ui.theme.OnChosenGreen
import com.tomli.foodhelper.ui.theme.ProgressFill
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelloPage(navController: NavController, foodVM: FoodVM = viewModel(factory = FoodVM.factory)) {
    val context = LocalContext.current
    val birthday = remember { mutableStateOf("") }
    val height = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("") }
    val genderVariants = listOf("Мужской", "Женский")
    val activityLv = remember { mutableStateOf("") }
    val actLvVariants = listOf(ActivityLevel.Low.screenName, ActivityLevel.Middle.screenName, ActivityLevel.High.screenName)

    val isDatePick = remember { mutableStateOf(false) }

    val isRightInputLambda:()->Boolean = {isItRightInput(birthday.value,height.value, weight.value,
        gender.value, activityLv.value)}
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 20.dp).fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Text(text = "Здравствуйте!", fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text(text = "Перед началом работы введите свои параметры!")
            Row(modifier = Modifier.padding(vertical = 10.dp)){
                OutlinedTextField(value = birthday.value, onValueChange = {}, readOnly = true,
                    modifier=Modifier.fillMaxWidth(),
                    trailingIcon = {Text(text="Выбрать",color= ProgressFill, modifier=Modifier.padding(end=10.dp).clickable { isDatePick.value=true })},
                    label = {Text(text="Дата рождения")})
            }
            OutlinedTextField(value = height.value, onValueChange = {newVal->height.value=newVal},
                modifier=Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text(text="Рост, см")})
            Spacer(modifier=Modifier.height(10.dp))
            OutlinedTextField(value = weight.value, onValueChange = {newVal->weight.value=newVal},
                modifier=Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text(text="Вес, кг")})
            Spacer(modifier=Modifier.height(10.dp))
            Box{
                val dropDownGender = remember { mutableStateOf(false) }
                OutlinedTextField(value = gender.value, onValueChange = {},
                    trailingIcon = {
                        Image(painter = painterResource(if(dropDownGender.value) R.drawable.arrow_up else R.drawable.arrow_down), contentDescription = null, modifier = Modifier.padding(7.dp).size(25.dp)
                            .clickable { dropDownGender.value=!dropDownGender.value })
                    },
                    modifier=Modifier.fillMaxWidth(),
                    readOnly = true,
                    label = {Text(text="Пол")}
                )
                DropdownMenu(expanded = dropDownGender.value, onDismissRequest = {dropDownGender.value=false}) {
                    genderVariants.forEach{ item->
                        DropdownMenuItem(
                            text={Text(text=item)},
                            onClick = {gender.value=item; dropDownGender.value=false}
                        )
                    }
                }
            }
            Box{
                val dropDownActivity = remember { mutableStateOf(false) }
                OutlinedTextField(value = activityLv.value, onValueChange = {},
                    trailingIcon = {
                        Image(painter = painterResource(if(dropDownActivity.value) R.drawable.arrow_up else R.drawable.arrow_down), contentDescription = null, modifier = Modifier.padding(7.dp).size(25.dp)
                            .clickable { dropDownActivity.value=!dropDownActivity.value })
                    },
                    modifier=Modifier.fillMaxWidth(),
                    readOnly = true,
                    label = {Text(text="Уровень активности")}
                )
                DropdownMenu(expanded = dropDownActivity.value, onDismissRequest = {dropDownActivity.value=false}) {
                    actLvVariants.forEach{ item->
                        DropdownMenuItem(
                            text={Text(text=item)},
                            onClick = {activityLv.value=item; dropDownActivity.value=false}
                        )
                    }
                }
            }
            Spacer(modifier=Modifier.weight(1f))
            Card(colors = CardDefaults.cardColors(
                containerColor = if(isRightInputLambda()){ InterfaceGreen} else { Color(0xff9f9f9f) },
                contentColor = if(isRightInputLambda()){ OnChosenGreen} else { Color.White },
            ),
                onClick = {
                    if(isRightInputLambda()){
                        val sharedPrefs = context.getSharedPreferences("this_prefs", MODE_PRIVATE)
                        with(sharedPrefs.edit()){
                            putBoolean("isHaveUser", true)
                            apply()
                        }
                        Toast.makeText(context, "Добро пожаловать", Toast.LENGTH_LONG).show()
                        foodVM.setUser(birthday.value, height.value, weight.value, gender.value, activityLv.value)
                        navController.navigate("main_screen")
                    }else{
                        Toast.makeText(context, "Не все поля введены правильно", Toast.LENGTH_LONG).show()
                    }
                }, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)){
                Text(text="Далее", modifier = Modifier.padding(10.dp).fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }

    if(isDatePick.value){
        val selectedDate = remember { mutableStateOf(LocalDate.now()) }
        val state = rememberDatePickerState(initialSelectedDateMillis =selectedDate.value.toEpochDay())
        val context = LocalContext.current
        DatePickerDialog(
            onDismissRequest = { isDatePick.value=false },
            confirmButton = {
                Text("Выбрать", modifier = Modifier.padding(start=40.dp, end=10.dp).clickable{
                    if(isLocalDateAfterMillis(state.selectedDateMillis!!, LocalDate.now())){
                        birthday.value = convertMillisToDate(state.selectedDateMillis!!); isDatePick.value=false
                    }else{
                        Toast.makeText(context, "Выберите дату раньше текущей", Toast.LENGTH_LONG).show()
                    }})
            },
            dismissButton = {
                Text("Отмена", modifier = Modifier.clickable{isDatePick.value=false})
            }
        ) {
            DatePicker(
                state = state
            )
        }


    }
}


fun isItRightInput(bday: String, height: String, weight: String, gender: String, activity: String): Boolean{
    return try{
        height!="" && height.toFloat()>0 && weight!="" && weight.toFloat()>0
                && bday!="" && gender!="" && activity!=""
    }catch (e: Exception){
        false
    }
}