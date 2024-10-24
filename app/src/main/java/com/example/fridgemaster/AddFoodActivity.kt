package com.example.fridgemaster

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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    @Composable
    fun AddFoodScreen() {
        var foodName by remember { mutableStateOf("") }
        var foodQuantity by remember { mutableStateOf(1) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Add Food Item", style = MaterialTheme.typography.headlineMedium)

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

                    Spacer(modifier = Modifier.width(4.dp))

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

                    Spacer(modifier = Modifier.width(4.dp))

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

            Spacer(modifier = Modifier.height(32.dp))

            // Add Food Button
            Button(
                onClick = {
                    // Handle adding food logic here
                    // save to database/inventory
                    // then
                    foodQuantity = 1
                    foodName = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Food")
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
