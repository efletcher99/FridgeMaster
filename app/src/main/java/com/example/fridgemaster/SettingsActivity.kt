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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.fridgemaster.ui.theme.FridgeMasterTheme
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var foodItemDao: FoodItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseInstance.getDatabase(applicationContext)
        foodItemDao = db.foodItemDao()

        setContent {
            FridgeMasterTheme {
                SettingsScreen()
            }
        }
    }

    @Composable
    fun SettingsScreen() {
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
                // Top Row with Back Button and Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                        startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Spacer(modifier = Modifier.width(40.dp))
                    Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
                }

                Spacer(modifier = Modifier.weight(.05f))

                // Clear Inventory Button
                Button(
                    onClick = {
                    lifecycleScope.launch {
                        foodItemDao.clearAllFoodItems()
                                }
                              },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Clear Inventory", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun InventoryPreview() {
        FridgeMasterTheme {
            SettingsScreen()
        }
    }

}
