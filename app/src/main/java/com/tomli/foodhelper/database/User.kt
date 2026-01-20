package com.tomli.foodhelper.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val birthday: String?,
    val height: String?,
    val weight: String?,
    val gender: String?,
    val activityLv: String?
)

//birthday, height, weight, gender, activityLv