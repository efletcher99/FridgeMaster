package com.example.fridgemaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.fridgemaster.ui.theme.FridgeMasterTheme
import kotlinx.coroutines.launch

class CheckInventoryActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var foodItemDao: FoodItemDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize Room
        db = DatabaseInstance.getDatabase(applicationContext)
        foodItemDao = db.foodItemDao()

        lifecycleScope.launch{
            val items = foodItemDao.getAllFoodItems()
            setContent {
                FridgeMasterTheme {
                    InventoryScreen(items)
                }
            }
        }
    }

    @Composable
    fun InventoryScreen(initialItems: List<FoodItem> = emptyList()) {
        var foodItems by remember { mutableStateOf(initialItems) }
        var isDropdownExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        val intent = Intent(this@CheckInventoryActivity, MainActivity::class.java)
                        startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Spacer(modifier = Modifier.width(40.dp))
                    Text(text = "Check Inventory", style = MaterialTheme.typography.headlineMedium)
                }

                foodItems.forEach { item ->
                    Text(text = "${item.name}, Quantity: ${item.quantity} ${item.unit}, Expiration Date: ${item.expirationDate}")
                }
            }

            Button(
                onClick = { isDropdownExpanded = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(text = "Sort Inventory")
            }
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        isDropdownExpanded = false
                        foodItems = foodItems.sortedBy { it.name }
                    },
                    text = { Text("Sort by Name") }
                )
                DropdownMenuItem(
                    onClick = {
                        isDropdownExpanded = false
                        foodItems = foodItems.sortedBy { it.expirationDate }
                    },
                    text = { Text("Sort by Expiration Date") }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun InventoryPreview() {
        FridgeMasterTheme {
            InventoryScreen()
        }
    }
}

