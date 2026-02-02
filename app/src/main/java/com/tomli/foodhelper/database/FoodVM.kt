package com.tomli.foodhelper.database

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.tomli.foodhelper.Applic
import com.tomli.foodhelper.screens.calculateCPFC
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FoodVM(val database: FoodDB) : ViewModel() {
    val allFoodDB = database.dao.allFoodDB()
    val allFoodDiary = database.dao.getAllFoodDiary()

    fun setUser(birthday: String, height: String, weight: String, gender: String, activityLv: String)=viewModelScope.launch{
        database.dao.setUser(birthday, height, weight, gender, activityLv)
    }

    fun getUser(onReturn:(user: User)->Unit)=viewModelScope.launch {
        val user = database.dao.getOneUser()
        onReturn(user)
    }

    fun updateUser(user: User)=viewModelScope.launch {
        database.dao.updateUser(user)
    }

    fun addFoodToDB(food: FoodInfo)=viewModelScope.launch {
        database.dao.addFoodToDB(food.name!!, food.description, food.grams!!, food.calories!!,
            food.proteins!!, food.fats!!, food.carbohydrates!!)
    }

    fun updateFoodInDB(food: FoodInfo)=viewModelScope.launch {
        database.dao.updateFoodDB(food)
    }

    fun deleteFoodInDB(food: FoodInfo)=viewModelScope.launch {
        database.dao.deleteFoodInDB(food)
    }

    fun getLastFoodDiaryDay(date: String, onReturn:(foodDiaryOne: FoodDiaryDB)->Unit, jsonReturn:(list: FoodDiaryList)->Unit)=viewModelScope.launch {
        val foodDiaryOne = database.dao.getLastFoodDiaryDay()
        if(foodDiaryOne==null || foodDiaryOne.date!=date){
            val def = FoodDiaryDB(null, date, 0F, 0F, 0F, 0F, 0F, 0F,
                0F, 0F, Json.encodeToString(FoodDiaryList(mutableListOf())))
            onReturn(def)
            jsonReturn(FoodDiaryList(mutableListOf()))
            database.dao.insertFoodDiaryNew(def)
        }else{
            onReturn(foodDiaryOne)
            jsonReturn(Json.decodeFromString<FoodDiaryList>(foodDiaryOne.jsonInfo!!))
        }
    }

    fun updateFoodDiaryToday(foodDiaryOne: FoodDiaryDB, newFoodList: FoodDiaryList)=viewModelScope.launch{
        val listJson = Json.encodeToString(newFoodList)
        database.dao.updateFoodDiaryToday(foodDiaryOne.copy(jsonInfo = listJson))
    }

    companion object{
        val factory: ViewModelProvider.Factory= object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as Applic).database
                return FoodVM(database) as T
            }
        }
    }
}