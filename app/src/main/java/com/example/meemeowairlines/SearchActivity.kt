package com.example.meemeowairlines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meemeowairlines.ui.theme.YourAppTheme

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val departure = intent.getStringExtra("departure")
        val arrival = intent.getStringExtra("arrival")
        val departureDate = intent.getStringExtra("departureDate")
        val arrivalDate = intent.getStringExtra("arrivalDate")
        val flightClass = intent.getStringExtra("flightClass")

        setContent {
            YourAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SearchScreen(departure, arrival, departureDate, arrivalDate, flightClass)
                }
            }
        }
    }
}

@Composable
fun SearchScreen(departure: String?, arrival: String?, departureDate: String?, arrivalDate: String?, flightClass: String?) {
    val routes = listOf(
        "MNL - CEB", "MNL - CRK", "MNL - DVO", "MNL - ILO", "MNL - KLO", "MNL - PPS", "MNL - ZAM", "MNL - GES", "MNL - BCD",
        "CEB - MNL", "CEB - CRK", "CEB - DVO", "CEB - ILO", "CEB - KLO", "CEB - PPS", "CEB - ZAM", "CEB - GES", "CEB - BCD",
        "CRK - MNL", "CRK - CEB", "CRK - DVO", "CRK - ILO", "CRK - KLO", "CRK - PPS", "CRK - ZAM", "CRK - GES", "CRK - BCD",
        "DVO - MNL", "DVO - CEB", "DVO - CRK", "DVO - ILO", "DVO - KLO", "DVO - PPS", "DVO - ZAM", "DVO - GES", "DVO - BCD",
        "ILO - MNL", "ILO - CEB", "ILO - CRK", "ILO - DVO", "ILO - KLO", "ILO - PPS", "ILO - ZAM", "ILO - GES", "ILO - BCD",
        "KLO - MNL", "KLO - CEB", "KLO - CRK", "KLO - DVO", "KLO - ILO", "KLO - PPS", "KLO - ZAM", "KLO - GES", "KLO - BCD",
        "PPS - MNL", "PPS - CEB", "PPS - CRK", "PPS - DVO", "PPS - ILO", "PPS - KLO", "PPS - ZAM", "PPS - GES", "PPS - BCD",
        "ZAM - MNL", "ZAM - CEB", "ZAM - CRK", "ZAM - DVO", "ZAM - ILO", "ZAM - KLO", "ZAM - PPS", "ZAM - GES", "ZAM - BCD",
        "GES - MNL", "GES - CEB", "GES - CRK", "GES - DVO", "GES - ILO", "GES - KLO", "GES - PPS", "GES - ZAM", "GES - BCD",
        "BCD - MNL", "BCD - CEB", "BCD - CRK", "BCD - DVO", "BCD - ILO", "BCD - KLO", "BCD - PPS", "BCD - ZAM", "BCD - GES"
    )

    val filteredRoutes = routes.filter {
        it.startsWith(departure ?: "") && it.endsWith(arrival ?: "")
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(filteredRoutes) { route ->
            RouteCard(route = route, departureDate = departureDate, arrivalDate = arrivalDate)
        }
    }
}

@Composable
fun RouteCard(route: String, departureDate: String?, arrivalDate: String?) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = route, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Departure: ${departureDate ?: "N/A"}", fontSize = 14.sp)
            Text(text = "Arrival: ${arrivalDate ?: "N/A"}", fontSize = 14.sp)
        }
    }
}
