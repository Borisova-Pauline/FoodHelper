package com.tomli.foodhelper.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodInfo(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String?,
    val description: String?,
    val grams: Float?,
    val calories: Float?,
    val proteins: Float?, //белки
    val fats: Float?, //жиры
    val carbohydrates: Float? //углеводы
)
