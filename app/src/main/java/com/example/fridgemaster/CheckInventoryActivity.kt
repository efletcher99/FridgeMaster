package com.example.fridgemaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fridgemaster.ui.theme.FridgeMasterTheme

class CheckInventoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Composable
        fun Greeting(name: String, modifier: Modifier = Modifier) {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
        }

        @Composable
        fun GreetingPreview() {
            FridgeMasterTheme {
                Greeting("Android")
            }
        }
    }
}