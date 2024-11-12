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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.fridgemaster.ui.theme.FridgeMasterTheme
import kotlinx.coroutines.launch

class AddFoodActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var foodItemDao: FoodItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize Room
        db = DatabaseInstance.getDatabase(applicationContext)
        foodItemDao = db.foodItemDao()

        setContent {
            FridgeMasterTheme {
                AddFoodScreen()
            }
        }
    }

    data class FoodItem(
        var name: String,
        var quantity: Int,
        var unit: String,
        var expirationDate: String
    )

    @Composable
    fun AddFoodScreen() {
        var foodName by remember { mutableStateOf("") }
        var foodQuantity by remember { mutableIntStateOf(1) }
        var foodUnit by remember {mutableStateOf("")}
        var expirationDate by remember { mutableStateOf("") }
        var foodList by remember { mutableStateOf(listOf<FoodItem>()) }
        var expanded by remember { mutableStateOf(false) }
        val units = listOf("unit(s)", "oz", "cups", "liters", "lbs")
        var selectedUnit by remember { mutableStateOf(units[0]) }


        var selectedFoodItemIndex by remember { mutableStateOf(-1)}
        var isEditing by remember { mutableStateOf(false)}
        var editableName by remember { mutableStateOf("") }
        var editableQuantity by remember { mutableIntStateOf(1) }
        var editableUnit by remember { mutableStateOf("") }
        var editableExpirationDate by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    val intent = Intent(this@AddFoodActivity, MainActivity::class.java)
                    startActivity(intent)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = "Add Food",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Food Name TextField
            TextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    //.shadow(4.dp, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (foodQuantity > 1) foodQuantity-- },
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("-", style = TextStyle(fontSize = 20.sp, color = Color.White))
                }

                Text(
                    text = foodQuantity.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.width(30.dp),
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = { foodQuantity++ },
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+", style = TextStyle(fontSize = 20.sp, color = Color.White))
                }

                Box(modifier = Modifier.weight(1f)) {
                    TextButton(onClick = { expanded = true }) {
                        Text(selectedUnit)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        units.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    selectedUnit = unit
                                    expanded = false
                                    foodUnit = unit
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = expirationDate,
                onValueChange = { expirationDate = it },
                label = { Text("Expiration Date (e.g., 3/21/2024)") },
                modifier = Modifier
                    .fillMaxWidth()
                    //.shadow(4.dp, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (foodName.isNotEmpty() && expirationDate.isNotEmpty()) {
                        val newItem = FoodItem(foodName, foodQuantity, foodUnit, expirationDate)
                        foodList = foodList.toMutableList().apply { add(newItem) }
                        foodName = ""
                        foodQuantity = 1
                        foodUnit = units[0]
                        expirationDate = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    //.shadow(4.dp, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3A539B))
            ) {
                Text(text = "Add Food")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(foodList) {index, item ->
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
                            //.shadow(2.dp, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "${item.name}, Quantity: ${item.quantity} ${item.unit}, Expiration: ${item.expirationDate}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            if (isEditing && selectedFoodItemIndex >= 0){

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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
                        label = { Text("Edit Quantity")},
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
                        label = { Text("Expiration Date")},
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Update the selected item in foodList
                            if (selectedFoodItemIndex >= 0) {
                                val updatedItem = FoodItem(editableName, editableQuantity, editableUnit, editableExpirationDate)
                                foodList = foodList.toMutableList().apply {
                                    set(selectedFoodItemIndex, updatedItem)
                                }
                                isEditing = false
                                selectedFoodItemIndex = -1
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text("Save Changes")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    lifecycleScope.launch {
                        for (item in foodList) {
                            val foodItem = com.example.fridgemaster.FoodItem(name = item.name, quantity = item.quantity, unit = item.unit, expirationDate = item.expirationDate)
                            foodItemDao.insertFoodItem(foodItem)
                        }
                        foodList = listOf()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF3A539B))
            ) {
                Text(text = "Save Food to Inventory")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun AddFoodPreview() {
        FridgeMasterTheme {
            AddFoodScreen()
        }
    }
}