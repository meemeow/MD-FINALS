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
        val passengerCount = intent.getIntExtra("passengerCount", 1)

        val routes = if (tripType == "Round Trip") {
            listOf(
                Pair("Manila (MNL) - Cebu (CEB)", 1500),
                Pair("Manila (MNL) - Angeles City (CRK)", 1300),
                Pair("Manila (MNL) - Davao City (DVO)", 1800),
                Pair("Manila (MNL) - Iloilo City (ILO)", 1400),
                Pair("Manila (MNL) - Kalibo (KLO)", 1600),
                Pair("Manila (MNL) - Puerto Princesa (PPS)", 2000),
                Pair("Manila (MNL) - Zamboanga City (ZAM)", 2100),
                Pair("Manila (MNL) - General Santos City (GES)", 2200),
                Pair("Manila (MNL) - Bacolod City (BCD)", 1700),

                Pair("Cebu (CEB) - Manila (MNL)", 1600),
                Pair("Cebu (CEB) - Angeles City (CRK)", 1400),
                Pair("Cebu (CEB) - Davao City (DVO)", 1900),
                Pair("Cebu (CEB) - Iloilo City (ILO)", 1500),
                Pair("Cebu (CEB) - Kalibo (KLO)", 1700),
                Pair("Cebu (CEB) - Puerto Princesa (PPS)", 2100),
                Pair("Cebu (CEB) - Zamboanga City (ZAM)", 2200),
                Pair("Cebu (CEB) - General Santos City (GES)", 2300),
                Pair("Cebu (CEB) - Bacolod City (BCD)", 1800),

                Pair("Angeles City (CRK) - Manila (MNL)", 1400),
                Pair("Angeles City (CRK) - Cebu (CEB)", 1200),
                Pair("Angeles City (CRK) - Davao City (DVO)", 1700),
                Pair("Angeles City (CRK) - Iloilo City (ILO)", 1300),
                Pair("Angeles City (CRK) - Kalibo (KLO)", 1500),
                Pair("Angeles City (CRK) - Puerto Princesa (PPS)", 1900),
                Pair("Angeles City (CRK) - Zamboanga City (ZAM)", 2000),
                Pair("Angeles City (CRK) - General Santos City (GES)", 2100),
                Pair("Angeles City (CRK) - Bacolod City (BCD)", 1600),

                Pair("Davao City (DVO) - Manila (MNL)", 1900),
                Pair("Davao City (DVO) - Cebu (CEB)", 1700),
                Pair("Davao City (DVO) - Angeles City (CRK)", 2200),
                Pair("Davao City (DVO) - Iloilo City (ILO)", 1800),
                Pair("Davao City (DVO) - Kalibo (KLO)", 2000),
                Pair("Davao City (DVO) - Puerto Princesa (PPS)", 2400),
                Pair("Davao City (DVO) - Zamboanga City (ZAM)", 2500),
                Pair("Davao City (DVO) - General Santos City (GES)", 2600),
                Pair("Davao City (DVO) - Bacolod City (BCD)", 2100),

                Pair("Iloilo City (ILO) - Manila (MNL)", 1500),
                Pair("Iloilo City (ILO) - Cebu (CEB)", 1300),
                Pair("Iloilo City (ILO) - Angeles City (CRK)", 1800),
                Pair("Iloilo City (ILO) - Davao City (DVO)", 1400),
                Pair("Iloilo City (ILO) - Kalibo (KLO)", 1600),
                Pair("Iloilo City (ILO) - Puerto Princesa (PPS)", 2000),
                Pair("Iloilo City (ILO) - Zamboanga City (ZAM)", 2100),
                Pair("Iloilo City (ILO) - General Santos City (GES)", 2200),
                Pair("Iloilo City (ILO) - Bacolod City (BCD)", 1700),

                Pair("Kalibo (KLO) - Manila (MNL)", 1700),
                Pair("Kalibo (KLO) - Cebu (CEB)", 1500),
                Pair("Kalibo (KLO) - Angeles City (CRK)", 2000),
                Pair("Kalibo (KLO) - Davao City (DVO)", 1600),
                Pair("Kalibo (KLO) - Iloilo City (ILO)", 1800),
                Pair("Kalibo (KLO) - Puerto Princesa (PPS)", 2200),
                Pair("Kalibo (KLO) - Zamboanga City (ZAM)", 2300),
                Pair("Kalibo (KLO) - General Santos City (GES)", 2400),
                Pair("Kalibo (KLO) - Bacolod City (BCD)", 1900),

                Pair("Puerto Princesa (PPS) - Manila (MNL)", 2100),
                Pair("Puerto Princesa (PPS) - Cebu (CEB)", 1900),
                Pair("Puerto Princesa (PPS) - Angeles City (CRK)", 2400),
                Pair("Puerto Princesa (PPS) - Davao City (DVO)", 2000),
                Pair("Puerto Princesa (PPS) - Iloilo City (ILO)", 2200),
                Pair("Puerto Princesa (PPS) - Kalibo (KLO)", 2600),
                Pair("Puerto Princesa (PPS) - Zamboanga City (ZAM)", 2700),
                Pair("Puerto Princesa (PPS) - General Santos City (GES)", 2800),
                Pair("Puerto Princesa (PPS) - Bacolod City (BCD)", 2300),

                Pair("Zamboanga City (ZAM) - Manila (MNL)", 2200),
                Pair("Zamboanga City (ZAM) - Cebu (CEB)", 2000),
                Pair("Zamboanga City (ZAM) - Angeles City (CRK)", 2500),
                Pair("Zamboanga City (ZAM) - Davao City (DVO)", 2100),
                Pair("Zamboanga City (ZAM) - Iloilo City (ILO)", 2300),
                Pair("Zamboanga City (ZAM) - Kalibo (KLO)", 2700),
                Pair("Zamboanga City (ZAM) - Puerto Princesa (PPS)", 2800),
                Pair("Zamboanga City (ZAM) - General Santos City (GES)", 2900),
                Pair("Zamboanga City (ZAM) - Bacolod City (BCD)", 2400),

                Pair("General Santos City (GES) - Manila (MNL)", 2300),
                Pair("General Santos City (GES) - Cebu (CEB)", 2100),
                Pair("General Santos City (GES) - Angeles City (CRK)", 2600),
                Pair("General Santos City (GES) - Davao City (DVO)", 2200),
                Pair("General Santos City (GES) - Iloilo City (ILO)", 2400),
                Pair("General Santos City (GES) - Kalibo (KLO)", 2800),
                Pair("General Santos City (GES) - Puerto Princesa (PPS)", 2900),
                Pair("General Santos City (GES) - Zamboanga City (ZAM)", 3000),
                Pair("General Santos City (GES) - Bacolod City (BCD)", 2500),

                Pair("Bacolod City (BCD) - Manila (MNL)", 1800),
                Pair("Bacolod City (BCD) - Cebu (CEB)", 1600),
                Pair("Bacolod City (BCD) - Angeles City (CRK)", 2100),
                Pair("Bacolod City (BCD) - Davao City (DVO)", 1700),
                Pair("Bacolod City (BCD) - Iloilo City (ILO)", 1900),
                Pair("Bacolod City (BCD) - Kalibo (KLO)", 2300),
                Pair("Bacolod City (BCD) - Puerto Princesa (PPS)", 2400),
                Pair("Bacolod City (BCD) - Zamboanga City (ZAM)", 2500),
                Pair("Bacolod City (BCD) - General Santos City (GES)", 2600)
            )
        } else {
                listOf(
                    Pair("Manila (MNL) - Cebu (CEB)", 1500),
                    Pair("Manila (MNL) - Angeles City (CRK)", 1300),
                    Pair("Manila (MNL) - Davao City (DVO)", 1800),
                    Pair("Manila (MNL) - Iloilo City (ILO)", 1400),
                    Pair("Manila (MNL) - Kalibo (KLO)", 1600),
                    Pair("Manila (MNL) - Puerto Princesa (PPS)", 2000),
                    Pair("Manila (MNL) - Zamboanga City (ZAM)", 2100),
                    Pair("Manila (MNL) - General Santos City (GES)", 2200),
                    Pair("Manila (MNL) - Bacolod City (BCD)", 1700),

                    Pair("Cebu (CEB) - Manila (MNL)", 1600),
                    Pair("Cebu (CEB) - Angeles City (CRK)", 1400),
                    Pair("Cebu (CEB) - Davao City (DVO)", 1900),
                    Pair("Cebu (CEB) - Iloilo City (ILO)", 1500),
                    Pair("Cebu (CEB) - Kalibo (KLO)", 1700),
                    Pair("Cebu (CEB) - Puerto Princesa (PPS)", 2100),
                    Pair("Cebu (CEB) - Zamboanga City (ZAM)", 2200),
                    Pair("Cebu (CEB) - General Santos City (GES)", 2300),
                    Pair("Cebu (CEB) - Bacolod City (BCD)", 1800),

                    Pair("Angeles City (CRK) - Manila (MNL)", 1400),
                    Pair("Angeles City (CRK) - Cebu (CEB)", 1200),
                    Pair("Angeles City (CRK) - Davao City (DVO)", 1700),
                    Pair("Angeles City (CRK) - Iloilo City (ILO)", 1300),
                    Pair("Angeles City (CRK) - Kalibo (KLO)", 1500),
                    Pair("Angeles City (CRK) - Puerto Princesa (PPS)", 1900),
                    Pair("Angeles City (CRK) - Zamboanga City (ZAM)", 2000),
                    Pair("Angeles City (CRK) - General Santos City (GES)", 2100),
                    Pair("Angeles City (CRK) - Bacolod City (BCD)", 1600),

                    Pair("Davao City (DVO) - Manila (MNL)", 1900),
                    Pair("Davao City (DVO) - Cebu (CEB)", 1700),
                    Pair("Davao City (DVO) - Angeles City (CRK)", 2200),
                    Pair("Davao City (DVO) - Iloilo City (ILO)", 1800),
                    Pair("Davao City (DVO) - Kalibo (KLO)", 2000),
                    Pair("Davao City (DVO) - Puerto Princesa (PPS)", 2400),
                    Pair("Davao City (DVO) - Zamboanga City (ZAM)", 2500),
                    Pair("Davao City (DVO) - General Santos City (GES)", 2600),
                    Pair("Davao City (DVO) - Bacolod City (BCD)", 2100),

                    Pair("Iloilo City (ILO) - Manila (MNL)", 1500),
                    Pair("Iloilo City (ILO) - Cebu (CEB)", 1300),
                    Pair("Iloilo City (ILO) - Angeles City (CRK)", 1800),
                    Pair("Iloilo City (ILO) - Davao City (DVO)", 1400),
                    Pair("Iloilo City (ILO) - Kalibo (KLO)", 1600),
                    Pair("Iloilo City (ILO) - Puerto Princesa (PPS)", 2000),
                    Pair("Iloilo City (ILO) - Zamboanga City (ZAM)", 2100),
                    Pair("Iloilo City (ILO) - General Santos City (GES)", 2200),
                    Pair("Iloilo City (ILO) - Bacolod City (BCD)", 1700),

                    Pair("Kalibo (KLO) - Manila (MNL)", 1700),
                    Pair("Kalibo (KLO) - Cebu (CEB)", 1500),
                    Pair("Kalibo (KLO) - Angeles City (CRK)", 2000),
                    Pair("Kalibo (KLO) - Davao City (DVO)", 1600),
                    Pair("Kalibo (KLO) - Iloilo City (ILO)", 1800),
                    Pair("Kalibo (KLO) - Puerto Princesa (PPS)", 2200),
                    Pair("Kalibo (KLO) - Zamboanga City (ZAM)", 2300),
                    Pair("Kalibo (KLO) - General Santos City (GES)", 2400),
                    Pair("Kalibo (KLO) - Bacolod City (BCD)", 1900),

                    Pair("Puerto Princesa (PPS) - Manila (MNL)", 2100),
                    Pair("Puerto Princesa (PPS) - Cebu (CEB)", 1900),
                    Pair("Puerto Princesa (PPS) - Angeles City (CRK)", 2400),
                    Pair("Puerto Princesa (PPS) - Davao City (DVO)", 2000),
                    Pair("Puerto Princesa (PPS) - Iloilo City (ILO)", 2200),
                    Pair("Puerto Princesa (PPS) - Kalibo (KLO)", 2600),
                    Pair("Puerto Princesa (PPS) - Zamboanga City (ZAM)", 2700),
                    Pair("Puerto Princesa (PPS) - General Santos City (GES)", 2800),
                    Pair("Puerto Princesa (PPS) - Bacolod City (BCD)", 2300),

                    Pair("Zamboanga City (ZAM) - Manila (MNL)", 2200),
                    Pair("Zamboanga City (ZAM) - Cebu (CEB)", 2000),
                    Pair("Zamboanga City (ZAM) - Angeles City (CRK)", 2500),
                    Pair("Zamboanga City (ZAM) - Davao City (DVO)", 2100),
                    Pair("Zamboanga City (ZAM) - Iloilo City (ILO)", 2300),
                    Pair("Zamboanga City (ZAM) - Kalibo (KLO)", 2700),
                    Pair("Zamboanga City (ZAM) - Puerto Princesa (PPS)", 2800),
                    Pair("Zamboanga City (ZAM) - General Santos City (GES)", 2900),
                    Pair("Zamboanga City (ZAM) - Bacolod City (BCD)", 2400),

                    Pair("General Santos City (GES) - Manila (MNL)", 2300),
                    Pair("General Santos City (GES) - Cebu (CEB)", 2100),
                    Pair("General Santos City (GES) - Angeles City (CRK)", 2600),
                    Pair("General Santos City (GES) - Davao City (DVO)", 2200),
                    Pair("General Santos City (GES) - Iloilo City (ILO)", 2400),
                    Pair("General Santos City (GES) - Kalibo (KLO)", 2800),
                    Pair("General Santos City (GES) - Puerto Princesa (PPS)", 2900),
                    Pair("General Santos City (GES) - Zamboanga City (ZAM)", 3000),
                    Pair("General Santos City (GES) - Bacolod City (BCD)", 2500),

                    Pair("Bacolod City (BCD) - Manila (MNL)", 1800),
                    Pair("Bacolod City (BCD) - Cebu (CEB)", 1600),
                    Pair("Bacolod City (BCD) - Angeles City (CRK)", 2100),
                    Pair("Bacolod City (BCD) - Davao City (DVO)", 1700),
                    Pair("Bacolod City (BCD) - Iloilo City (ILO)", 1900),
                    Pair("Bacolod City (BCD) - Kalibo (KLO)", 2300),
                    Pair("Bacolod City (BCD) - Puerto Princesa (PPS)", 2400),
                    Pair("Bacolod City (BCD) - Zamboanga City (ZAM)", 2500),
                    Pair("Bacolod City (BCD) - General Santos City (GES)", 2600)
                )
        }


        val filteredRoutes = routes.filter { route ->
            val departureAirport = departure?.substringBefore(", ") ?: ""
            val arrivalAirport = arrival?.substringBefore(", ") ?: ""
            route.first.startsWith(departureAirport) && route.first.endsWith(arrivalAirport)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = RouteAdapter(this, filteredRoutes, departureDate, if (tripType == "Round Trip") arrivalDate else null, tripType, flightClass, passengerCount)
    }
}