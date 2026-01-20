package com.tomli.foodhelper.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, FoodInfo::class], version = 1,
    exportSchema = true, autoMigrations = [])
abstract class FoodDB : RoomDatabase() {
    abstract val dao: com.tomli.foodhelper.database.Dao
    companion object{
        fun createDB(context: Context): FoodDB{
            return Room.databaseBuilder(context, FoodDB::class.java, "app_db.db")
                .createFromAsset("app_db.db").build()
        }
    }
}