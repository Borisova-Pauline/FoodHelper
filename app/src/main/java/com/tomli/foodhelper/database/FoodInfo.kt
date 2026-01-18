package com.tomli.foodhelper.database

data class FoodInfo(
    val id: Int?,
    val name: String?,
    val description: String?,
    val grams: Float?,
    val calories: Float?,
    val proteins: Float?, //белки
    val fats: Float?, //жиры
    val carbohydrates: Float? //углеводы
)
