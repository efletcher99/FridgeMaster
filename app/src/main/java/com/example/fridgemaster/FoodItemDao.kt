package com.example.fridgemaster

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodItemDao {
    @Insert
    suspend fun insertFoodItem(foodItem: FoodItem)

    @Query("SELECT * FROM food_items")
    suspend fun getAllFoodItems(): List<FoodItem>

    @Query("DELETE FROM food_items WHERE id = :id")
    suspend fun deleteFoodItemById(id: Int)

    @Query ("DELETE FROM food_items")
    suspend fun clearAllFoodItems()

}
