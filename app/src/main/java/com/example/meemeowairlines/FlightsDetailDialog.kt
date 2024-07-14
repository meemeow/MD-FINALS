package com.example.meemeowairlines

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class FlightDetailsDialog(
    context: Context,
    private val route: String,
    private val departureDate: String?,
    private val arrivalDate: String?,
    private val tripType: String?,
    private val flightClass: String?,
    private val pricePerPassenger: Double, // Added pricePerPassenger parameter
    private val passengerCount: Int // Added passengerCount parameter
) : Dialog(context) {

    private lateinit var sharedPreferences: SharedPreferences
    private val dbManager = DatabaseManager(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_flight_details)

        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val textDeparture = findViewById<TextView>(R.id.textDeparture)
        val textArrival = findViewById<TextView>(R.id.textArrival)
        val textDepartureDate = findViewById<TextView>(R.id.textDepartureDate)
        val textArrivalDate = findViewById<TextView>(R.id.textArrivalDate)
        val textFlightClass = findViewById<TextView>(R.id.textFlightClass)
        val spinnerTime = findViewById<Spinner>(R.id.spinnerTime)
        val buttonBook = findViewById<Button>(R.id.buttonBook)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)

        val times = listOf("7:00am", "2:00pm", "8:00pm")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTime.adapter = adapter

        val textTotalPrice = findViewById<TextView>(R.id.textTotalPrice)
        val totalPrice = pricePerPassenger * passengerCount
        textTotalPrice.text = "Total Price: PHP $totalPrice"

        textDeparture.text = "Departure: ${route.split(" - ")[0]}"
        textArrival.text = "Arrival: ${route.split(" - ")[1]}"
        textDepartureDate.text = "Departure Date: ${departureDate ?: "N/A"}"
        if (tripType == "Round Trip") {
            textArrivalDate.text = "Arrival Date: ${arrivalDate ?: "N/A"}"
            textArrivalDate.visibility = View.VISIBLE
        } else {
            textArrivalDate.visibility = View.GONE
        }
        textFlightClass.text = "Class: ${flightClass ?: "N/A"}"

        buttonBook.setOnClickListener {
            val userId = sharedPreferences.getLong("userId", -1)
            if (userId == -1L) {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cursor = dbManager.getUser(userId)
            if (cursor != null && cursor.moveToFirst()) {
                val firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LAST_NAME))
                val passengerName = "$firstName $lastName"
                cursor.close()

                val time = spinnerTime.selectedItem.toString()
                val departure = route.split(" - ")[0]
                val arrival = route.split(" - ")[1]
                val totalPrice = passengerCount * pricePerPassenger // Calculate total price

                // Generate booking reference and ticket number
                val bookingReference = generateBookingReference()
                val ticketNumber = generateTicketNumber(flightClass)

                val result = dbManager.insertBooking(
                    bookingReference, ticketNumber, "AIR34-78", passengerName, time,
                    departure, arrival, departureDate ?: "", arrivalDate, flightClass ?: "",
                    passengerCount, totalPrice // Insert passenger count and total price
                )

                if (result != -1L) {
                    // Booking inserted successfully
                    Toast.makeText(context, "Booking successful!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                    dismiss()
                } else {
                    // Handle the error
                    Toast.makeText(context, "Booking failed!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "User details not found", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun generateBookingReference(): String {
        val lastBookingId = dbManager.getLastBookingId()
        val incrementedId = String.format("%05d", lastBookingId + 1)
        return "ARL-CAT$incrementedId"
    }

    private fun generateTicketNumber(flightClass: String?): String {
        val lastTicketId = dbManager.getLastTicketId()
        val incrementedId = String.format("%05d", lastTicketId + 1)
        val date = SimpleDateFormat("MMyy", Locale.getDefault()).format(Date())
        val classCode = when (flightClass) {
            "Business" -> "BUS"
            "First Class" -> "FRS"
            else -> "ECO"
        }
        return "MEOAIR$date-$classCode$incrementedId"
    }
}
