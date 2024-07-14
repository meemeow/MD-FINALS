package com.example.meemeowairlines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val departure = intent.getStringExtra("departure")
        val arrival = intent.getStringExtra("arrival")
        val departureDate = intent.getStringExtra("departureDate")
        val arrivalDate = intent.getStringExtra("arrivalDate")
        val flightClass = intent.getStringExtra("flightClass")
        val tripType = intent.getStringExtra("tripType")

        val routes = if (tripType == "Round Trip") {
            listOf(
                "Manila (MNL) - Cebu (CEB)", "Manila (MNL) - Angeles City (CRK)", "Manila (MNL) - Davao City (DVO)", "Manila (MNL) - Iloilo City (ILO)", "Manila (MNL) - Kalibo (KLO)", "Manila (MNL) - Puerto Princesa (PPS)", "Manila (MNL) - Zamboanga City (ZAM)", "Manila (MNL) - General Santos City (GES)", "Manila (MNL) - Bacolod City (BCD)",
                "Cebu (CEB) - Manila (MNL)", "Cebu (CEB) - Angeles City (CRK)", "Cebu (CEB) - Davao City (DVO)", "Cebu (CEB) - Iloilo City (ILO)", "Cebu (CEB) - Kalibo (KLO)", "Cebu (CEB) - Puerto Princesa (PPS)", "Cebu (CEB) - Zamboanga City (ZAM)", "Cebu (CEB) - General Santos City (GES)", "Cebu (CEB) - Bacolod City (BCD)",
                "Angeles City (CRK) - Manila (MNL)", "Angeles City (CRK) - Cebu (CEB)", "Angeles City (CRK) - Davao City (DVO)", "Angeles City (CRK) - Iloilo City (ILO)", "Angeles City (CRK) - Kalibo (KLO)", "Angeles City (CRK) - Puerto Princesa (PPS)", "Angeles City (CRK) - Zamboanga City (ZAM)", "Angeles City (CRK) - General Santos City (GES)", "Angeles City (CRK) - Bacolod City (BCD)",
                "Davao City (DVO) - Manila (MNL)", "Davao City (DVO) - Cebu (CEB)", "Davao City (DVO) - Angeles City (CRK)", "Davao City (DVO) - Iloilo City (ILO)", "Davao City (DVO) - Kalibo (KLO)", "Davao City (DVO) - Puerto Princesa (PPS)", "Davao City (DVO) - Zamboanga City (ZAM)", "Davao City (DVO) - General Santos City (GES)", "Davao City (DVO) - Bacolod City (BCD)",
                "Iloilo City (ILO) - Manila (MNL)", "Iloilo City (ILO) - Cebu (CEB)", "Iloilo City (ILO) - Angeles City (CRK)", "Iloilo City (ILO) - Davao City (DVO)", "Iloilo City (ILO) - Kalibo (KLO)", "Iloilo City (ILO) - Puerto Princesa (PPS)", "Iloilo City (ILO) - Zamboanga City (ZAM)", "Iloilo City (ILO) - General Santos City (GES)", "Iloilo City (ILO) - Bacolod City (BCD)",
                "Kalibo (KLO) - Manila (MNL)", "Kalibo (KLO) - Cebu (CEB)", "Kalibo (KLO) - Angeles City (CRK)", "Kalibo (KLO) - Davao City (DVO)", "Kalibo (KLO) - Iloilo City (ILO)", "Kalibo (KLO) - Puerto Princesa (PPS)", "Kalibo (KLO) - Zamboanga City (ZAM)", "Kalibo (KLO) - General Santos City (GES)", "Kalibo (KLO) - Bacolod City (BCD)",
                "Puerto Princesa (PPS) - Manila (MNL)", "Puerto Princesa (PPS) - Cebu (CEB)", "Puerto Princesa (PPS) - Angeles City (CRK)", "Puerto Princesa (PPS) - Davao City (DVO)", "Puerto Princesa (PPS) - Iloilo City (ILO)", "Puerto Princesa (PPS) - Kalibo (KLO)", "Puerto Princesa (PPS) - Zamboanga City (ZAM)", "Puerto Princesa (PPS) - General Santos City (GES)", "Puerto Princesa (PPS) - Bacolod City (BCD)",
                "Zamboanga City (ZAM) - Manila (MNL)", "Zamboanga City (ZAM) - Cebu (CEB)", "Zamboanga City (ZAM) - Angeles City (CRK)", "Zamboanga City (ZAM) - Davao City (DVO)", "Zamboanga City (ZAM) - Iloilo City (ILO)", "Zamboanga City (ZAM) - Kalibo (KLO)", "Zamboanga City (ZAM) - Puerto Princesa (PPS)", "Zamboanga City (ZAM) - General Santos City (GES)", "Zamboanga City (ZAM) - Bacolod City (BCD)",
                "General Santos City (GES) - Manila (MNL)", "General Santos City (GES) - Cebu (CEB)", "General Santos City (GES) - Angeles City (CRK)", "General Santos City (GES) - Davao City (DVO)", "General Santos City (GES) - Iloilo City (ILO)", "General Santos City (GES) - Kalibo (KLO)", "General Santos City (GES) - Puerto Princesa (PPS)", "General Santos City (GES) - Zamboanga City (ZAM)", "General Santos City (GES) - Bacolod City (BCD)",
                "Bacolod City (BCD) - Manila (MNL)", "Bacolod City (BCD) - Cebu (CEB)", "Bacolod City (BCD) - Angeles City (CRK)", "Bacolod City (BCD) - Davao City (DVO)", "Bacolod City (BCD) - Iloilo City (ILO)", "Bacolod City (BCD) - Kalibo (KLO)", "Bacolod City (BCD) - Puerto Princesa (PPS)", "Bacolod City (BCD) - Zamboanga City (ZAM)", "Bacolod City (BCD) - General Santos City (GES)"
            )
        } else {
            listOf(
                "Manila (MNL) - Cebu (CEB)", "Manila (MNL) - Angeles City (CRK)", "Manila (MNL) - Davao City (DVO)", "Manila (MNL) - Iloilo City (ILO)", "Manila (MNL) - Kalibo (KLO)", "Manila (MNL) - Puerto Princesa (PPS)", "Manila (MNL) - Zamboanga City (ZAM)", "Manila (MNL) - General Santos City (GES)", "Manila (MNL) - Bacolod City (BCD)",
                "Cebu (CEB) - Manila (MNL)", "Cebu (CEB) - Angeles City (CRK)", "Cebu (CEB) - Davao City (DVO)", "Cebu (CEB) - Iloilo City (ILO)", "Cebu (CEB) - Kalibo (KLO)", "Cebu (CEB) - Puerto Princesa (PPS)", "Cebu (CEB) - Zamboanga City (ZAM)", "Cebu (CEB) - General Santos City (GES)", "Cebu (CEB) - Bacolod City (BCD)",
                "Angeles City (CRK) - Manila (MNL)", "Angeles City (CRK) - Cebu (CEB)", "Angeles City (CRK) - Davao City (DVO)", "Angeles City (CRK) - Iloilo City (ILO)", "Angeles City (CRK) - Kalibo (KLO)", "Angeles City (CRK) - Puerto Princesa (PPS)", "Angeles City (CRK) - Zamboanga City (ZAM)", "Angeles City (CRK) - General Santos City (GES)", "Angeles City (CRK) - Bacolod City (BCD)",
                "Davao City (DVO) - Manila (MNL)", "Davao City (DVO) - Cebu (CEB)", "Davao City (DVO) - Angeles City (CRK)", "Davao City (DVO) - Iloilo City (ILO)", "Davao City (DVO) - Kalibo (KLO)", "Davao City (DVO) - Puerto Princesa (PPS)", "Davao City (DVO) - Zamboanga City (ZAM)", "Davao City (DVO) - General Santos City (GES)", "Davao City (DVO) - Bacolod City (BCD)",
                "Iloilo City (ILO) - Manila (MNL)", "Iloilo City (ILO) - Cebu (CEB)", "Iloilo City (ILO) - Angeles City (CRK)", "Iloilo City (ILO) - Davao City (DVO)", "Iloilo City (ILO) - Kalibo (KLO)", "Iloilo City (ILO) - Puerto Princesa (PPS)", "Iloilo City (ILO) - Zamboanga City (ZAM)", "Iloilo City (ILO) - General Santos City (GES)", "Iloilo City (ILO) - Bacolod City (BCD)",
                "Kalibo (KLO) - Manila (MNL)", "Kalibo (KLO) - Cebu (CEB)", "Kalibo (KLO) - Angeles City (CRK)", "Kalibo (KLO) - Davao City (DVO)", "Kalibo (KLO) - Iloilo City (ILO)", "Kalibo (KLO) - Puerto Princesa (PPS)", "Kalibo (KLO) - Zamboanga City (ZAM)", "Kalibo (KLO) - General Santos City (GES)", "Kalibo (KLO) - Bacolod City (BCD)",
                "Puerto Princesa (PPS) - Manila (MNL)", "Puerto Princesa (PPS) - Cebu (CEB)", "Puerto Princesa (PPS) - Angeles City (CRK)", "Puerto Princesa (PPS) - Davao City (DVO)", "Puerto Princesa (PPS) - Iloilo City (ILO)", "Puerto Princesa (PPS) - Kalibo (KLO)", "Puerto Princesa (PPS) - Zamboanga City (ZAM)", "Puerto Princesa (PPS) - General Santos City (GES)", "Puerto Princesa (PPS) - Bacolod City (BCD)",
                "Zamboanga City (ZAM) - Manila (MNL)", "Zamboanga City (ZAM) - Cebu (CEB)", "Zamboanga City (ZAM) - Angeles City (CRK)", "Zamboanga City (ZAM) - Davao City (DVO)", "Zamboanga City (ZAM) - Iloilo City (ILO)", "Zamboanga City (ZAM) - Kalibo (KLO)", "Zamboanga City (ZAM) - Puerto Princesa (PPS)", "Zamboanga City (ZAM) - General Santos City (GES)", "Zamboanga City (ZAM) - Bacolod City (BCD)",
                "General Santos City (GES) - Manila (MNL)", "General Santos City (GES) - Cebu (CEB)", "General Santos City (GES) - Angeles City (CRK)", "General Santos City (GES) - Davao City (DVO)", "General Santos City (GES) - Iloilo City (ILO)", "General Santos City (GES) - Kalibo (KLO)", "General Santos City (GES) - Puerto Princesa (PPS)", "General Santos City (GES) - Zamboanga City (ZAM)", "General Santos City (GES) - Bacolod City (BCD)",
                "Bacolod City (BCD) - Manila (MNL)", "Bacolod City (BCD) - Cebu (CEB)", "Bacolod City (BCD) - Angeles City (CRK)", "Bacolod City (BCD) - Davao City (DVO)", "Bacolod City (BCD) - Iloilo City (ILO)", "Bacolod City (BCD) - Kalibo (KLO)", "Bacolod City (BCD) - Puerto Princesa (PPS)", "Bacolod City (BCD) - Zamboanga City (ZAM)", "Bacolod City (BCD) - General Santos City (GES)"
            )
        }


        val filteredRoutes = routes.filter { route ->
            val departureAirport = departure?.substringBefore(", ") ?: ""
            val arrivalAirport = arrival?.substringBefore(", ") ?: ""
            route.startsWith(departureAirport) && route.endsWith(arrivalAirport)
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = RouteAdapter(this, filteredRoutes, departureDate, if (tripType == "Round Trip") arrivalDate else null, tripType, flightClass)

    }
}
