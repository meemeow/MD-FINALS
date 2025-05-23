package com.example.meemeowairlines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.meemeowairlines.ui.theme.YourAppTheme

class FlightStatusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    FlightStatusScreen()
                }
            }
        }
    }
}

@Composable
fun FlightStatusScreen() {
    Text(text = "Flight Status")
}

@Preview(showBackground = true)
@Composable
fun FlightStatusScreenPreview() {
    YourAppTheme {
        FlightStatusScreen()
    }
}
