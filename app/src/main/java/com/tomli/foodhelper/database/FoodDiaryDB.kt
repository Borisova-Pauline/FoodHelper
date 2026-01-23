package com.tomli.foodhelper.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class FoodDiaryDB(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val date: String?,
    val needCalories: Float?,
    val haveCalories: Float?,
    val needProteins: Float?,
    val haveProteins: Float?,
    val needFats: Float?,
    val haveFats: Float?,
    val needCarbohydrates: Float?,
    val haveCarbohydrates: Float?,
    val jsonInfo: String?
)


@Serializable
data class FoodDiaryOne(
    val time: String,
    val food: FoodInfo
)

@Serializable
data class FoodDiaryList(
    val list: MutableList<FoodDiaryOne>
)