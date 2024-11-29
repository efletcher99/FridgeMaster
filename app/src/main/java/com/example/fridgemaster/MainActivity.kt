package com.example.fridgemaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fridgemaster.ui.theme.FridgeMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FridgeMasterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(innerPadding)
                }
            }
        }
    }

    @Composable
    fun MainScreen(innerPadding: PaddingValues) {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Fridge Master",
                    style = TextStyle(
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(200.dp))

                Button(onClick = {
                    val intent = Intent(this@MainActivity, AddFoodActivity::class.java)
                    startActivity(intent)
                }) {
                    Text(text = "Add Food")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val intent = Intent(this@MainActivity, CheckInventoryActivity::class.java)
                    startActivity(intent)
                }) {
                    Text(text = "Check Inventory")
                }

                /*Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val intent = Intent(this@MainActivity, GenerateRecipeActivity::class.java)
                    startActivity(intent)
                }) {
                    Text(text = "Generate Recipe")
                }*/
            }


            IconButton(
                onClick = {
                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(25.dp)

            ) {
                Icon(imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(48.dp))
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FridgeMasterTheme {
            MainScreen(PaddingValues())
        }
    }
}
