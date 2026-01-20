package com.tomli.foodhelper.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.tomli.foodhelper.Applic
import kotlinx.coroutines.launch

class FoodVM(val database: FoodDB) : ViewModel() {
    val allFoodDB = database.dao.allFoodDB()

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