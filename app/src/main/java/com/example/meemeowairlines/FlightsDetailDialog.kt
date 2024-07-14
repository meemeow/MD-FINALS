package com.example.meemeowairlines

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

class FlightDetailsDialog(
    context: Context,
    private val route: String,
    private val departureDate: String?,
    private val arrivalDate: String?,
    private val tripType: String?,
    private val flightClass: String? // Added flightClass parameter
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_flight_details)

        val textDeparture = findViewById<TextView>(R.id.textDeparture)
        val textArrival = findViewById<TextView>(R.id.textArrival)
        val textDepartureDate = findViewById<TextView>(R.id.textDepartureDate)
        val textArrivalDate = findViewById<TextView>(R.id.textArrivalDate)
        val textFlightClass = findViewById<TextView>(R.id.textFlightClass) // Add TextView for flight class
        val spinnerTime = findViewById<Spinner>(R.id.spinnerTime)
        val buttonBook = findViewById<Button>(R.id.buttonBook)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)

        val times = listOf("7:00am", "2:00pm", "8:00pm")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTime.adapter = adapter

        textDeparture.text = "Departure: ${route.split(" - ")[0]}"
        textArrival.text = "Arrival: ${route.split(" - ")[1]}"
        textDepartureDate.text = "Departure Date: ${departureDate ?: "N/A"}"
        if (tripType == "Round Trip") {
            textArrivalDate.text = "Arrival Date: ${arrivalDate ?: "N/A"}"
            textArrivalDate.visibility = View.VISIBLE
        } else {
            textArrivalDate.visibility = View.GONE
        }
        textFlightClass.text = "Class: ${flightClass ?: "N/A"}" // Bind flightClass

        buttonBook.setOnClickListener {
            // Handle booking logic
            dismiss()
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }
    }
}

