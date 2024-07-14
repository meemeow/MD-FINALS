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

class FlightDisruptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    FlightDisruptionScreen()
                }
            }
        }
    }
}

@Composable
fun FlightDisruptionScreen() {
    Text(text = "Flight Disruption")
}

@Preview(showBackground = true)
@Composable
fun FlightDisruptionScreenPreview() {
    YourAppTheme {
        FlightDisruptionScreen()
    }
}
