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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fridgemaster.ui.theme.FridgeMasterTheme

class AddFoodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FridgeMasterTheme {
                AddFoodScreen()
            }
        }
    }

    data class FoodItem(
        val name: String,
        val quantity: Int,
        val expirationDate: String
    )

    @Composable
    fun AddFoodScreen() {
        var foodName by remember { mutableStateOf("") }
        var foodQuantity by remember { mutableIntStateOf(1) }
        var expirationDate by remember {mutableStateOf("")}
        var foodList by remember { mutableStateOf(listOf<FoodItem>()) }

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
                    val intent = Intent(this@AddFoodActivity, MainActivity::class.java)
                    startActivity(intent)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,

                        contentDescription = "Back"
                    )
                }

                Spacer(
                    modifier = Modifier.width(
                        40.dp
                    )
                )
                Text(text = "Add Food Item", style = MaterialTheme.typography.headlineMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Food Name") },
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 16.dp) // Add some space between TextField and counter
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1.5f) // Make this row take the remaining space
                ) {
                    // Decrement Button
                    Button(
                        onClick = {
                            if (foodQuantity > 1) foodQuantity--
                        },
                        modifier = Modifier.size(50.dp),
                    ) {
                        Text(
                            text = "-",
                            style = TextStyle(fontSize= 20.sp, color = Color.White),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .align(Alignment.CenterVertically)
                    )
                    // Quantity Display
                    Text(
                        text = foodQuantity.toString(),
                        modifier = Modifier
                            .width(15.dp)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Increment Button
                    Button(
                        onClick = { foodQuantity++ },
                        modifier = Modifier.size(50.dp),
                    ) {
                        Text(
                            text = "+",
                            style = TextStyle(fontSize= 20.sp, color = Color.White),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = expirationDate,
                    onValueChange = {expirationDate = it},
                    label = { Text("Expiration Date                   ex. 3/21/2024") },
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Add Food Button
            Button(
                onClick = {
                    if (foodName.isNotEmpty() && expirationDate.isNotEmpty()) {
                        // Create new FoodItem and add to list
                        val newItem = FoodItem(foodName, foodQuantity, expirationDate)
                        foodList = foodList.toMutableList().apply { add(newItem) }
                        // Clear inputs
                        foodName = ""
                        foodQuantity = 1
                        expirationDate = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Food")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display the food list
            LazyColumn {
                items(foodList) { item ->
                    Text(text = "${item.name}, Quantity: ${item.quantity}, Expiration: ${item.expirationDate}")
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


            Button(
                onClick = {
                    // Handle save logic here
                },
                modifier = Modifier
                    .fillMaxWidth()
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