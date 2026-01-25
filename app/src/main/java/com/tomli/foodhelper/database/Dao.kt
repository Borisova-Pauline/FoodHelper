package com.tomli.foodhelper.database

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("insert into FoodInfo (name, description, grams, calories, proteins, fats, carbohydrates) " +
            "values (:name, :description, :grams, :calories, :proteins, :fats, :carbohydrates)")
    suspend fun addFoodToDB(name: String, description: String?, grams: Float, calories: Float, proteins: Float, fats: Float, carbohydrates: Float)

    @Update
    suspend fun updateFoodDB(food: FoodInfo)

    @Delete
    suspend fun deleteFoodInDB(food: FoodInfo)

    @Query("select * from FoodDiaryDB order by id desc limit 1")
    suspend fun getLastFoodDiaryDay(): FoodDiaryDB?

    @Update
    suspend fun updateFoodDiaryToday(foodDiaryDB: FoodDiaryDB)

    @Insert
    suspend fun insertFoodDiaryNew(foodDiaryDB: FoodDiaryDB)

    @Query("select * from FoodDiaryDB")
    fun getAllFoodDiary(): Flow<List<FoodDiaryDB>>
}