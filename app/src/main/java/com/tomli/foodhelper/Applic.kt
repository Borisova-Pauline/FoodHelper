package com.tomli.foodhelper

import android.app.Application
import com.tomli.foodhelper.database.FoodDB

class Applic: Application() {
    val database by lazy{ FoodDB.createDB(this) }
}