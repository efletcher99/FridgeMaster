package com.example.fridgemaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

        // Initialize Room
        db = DatabaseInstance.getDatabase(applicationContext)
        foodItemDao = db.foodItemDao()

        lifecycleScope.launch {
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
        var foodList by remember { mutableStateOf(initialItems) }
        var isDropdownExpanded by remember { mutableStateOf(false) }

        var selectedFoodItemIndex by remember { mutableIntStateOf(-1) }
        var isEditing by remember { mutableStateOf(false) }
        var editableName by remember { mutableStateOf("") }
        var editableQuantity by remember { mutableIntStateOf(1) }
        var editableUnit by remember { mutableStateOf("") }
        var editableExpirationDate by remember { mutableStateOf("") }

        val coroutineScope = rememberCoroutineScope()

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
                // Top bar with back button
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

                // Food list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    itemsIndexed(foodList) { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedFoodItemIndex = index
                                    isEditing = true
                                    editableName = item.name
                                    editableQuantity = item.quantity
                                    editableUnit = item.unit
                                    editableExpirationDate = item.expirationDate
                                }
                                .padding(horizontal = 8.dp)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "${item.name}, Quantity: ${item.quantity} ${item.unit}, Expiration: ${item.expirationDate}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                // Editing form
                if (isEditing && selectedFoodItemIndex >= 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            //.align(Alignment.BottomCenter)
                    ) {
                        TextField(
                            value = editableName,
                            onValueChange = { editableName = it },
                            label = { Text("Edit Food Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = editableQuantity.toString(),
                            onValueChange = { editableQuantity = it.toIntOrNull() ?: 1 },
                            label = { Text("Edit Quantity") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = editableUnit,
                            onValueChange = { editableUnit = it },
                            label = { Text("Edit Unit") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = editableExpirationDate,
                            onValueChange = { editableExpirationDate = it },
                            label = { Text("Expiration Date") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                    val selectedFoodItem = foodList[selectedFoodItemIndex]
                                    val updatedItem = FoodItem(
                                        selectedFoodItem.id,
                                        editableName,
                                        editableQuantity.coerceAtLeast(1),
                                        editableUnit,
                                        editableExpirationDate
                                    )
                                    // Update the list
                                    foodList = foodList.toMutableList().apply {
                                        set(selectedFoodItemIndex, updatedItem)
                                    }

                                    coroutineScope.launch {
                                        foodItemDao.updateFoodItem(updatedItem)
                                    }

                                    isEditing = false
                                    selectedFoodItemIndex = -1

                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Changes")
                        }

                        Button(
                            onClick = {
                                val selectedFoodItem = foodList[selectedFoodItemIndex]
                                foodList = foodList.toMutableList().apply {
                                    removeAt(selectedFoodItemIndex)
                                }

                                coroutineScope.launch {
                                    foodItemDao.deleteFoodItemById(selectedFoodItem.id.toInt())
                                }

                                isEditing = false
                                selectedFoodItemIndex = -1

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            Text("Delete Item")
                        }
                    }
                }
            }

            if (!isEditing) {
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
                            foodList = foodList.sortedBy { it.name }
                        },
                        text = { Text("Sort by Name") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            isDropdownExpanded = false
                            foodList = foodList.sortedBy { it.expirationDate }
                        },
                        text = { Text("Sort by Expiration Date") }
                    )
                }
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
