package com.example.fridgemaster

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.fridgemaster.ui.theme.FridgeMasterTheme
import kotlinx.coroutines.launch

class CheckInventoryActivity : ComponentActivity() {

    private lateinit var foodItemDao: FoodItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "fridge_master_database"
        ).build()

        foodItemDao = database.foodItemDao()

        setContent {
            FridgeMasterTheme {
                InventoryScreen(foodItemDao)
            }
        }
    }

    @Composable
    fun InventoryScreen(foodItemDao: FoodItemDao) {

        var foodItems by remember { mutableStateOf(emptyList<FoodItem>()) }

        Column(modifier = Modifier.fillMaxSize()) {

            Button(onClick = {
                lifecycleScope.launch {
                    foodItems = this@CheckInventoryActivity.foodItemDao.getAllFoodItems()
                    Log.d("InventoryScreen", "Retrieved ${foodItems.size} items from the database.")
                }
            }) {
                Text(text = "Show Inventory")
            }

            if(foodItems.isEmpty()){
                Text(text = "No items in inventory.")
            }else {
                foodItems.forEach { item ->
                    Text(text = "${item.name}, Quantity: ${item.quantity}, Expiration Date: ${item.expirationDate}")
                }
            }
        }
    }
}
