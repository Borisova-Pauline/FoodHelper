package com.tomli.foodhelper.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("select * from FoodInfo")
    fun allFoodDB(): Flow<List<FoodInfo>>

    @Update
    suspend fun updateUser(user: User)

    @Query("insert into user (birthday, height, weight, gender, activityLv) values (:birthday, :height, :weight, :gender, :activityLv)")
    suspend fun setUser(birthday: String, height: String, weight: String, gender: String, activityLv: String)

    @Query("select * from user order by id desc limit 1")
    suspend fun getOneUser(): User
}