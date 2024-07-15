package com.example.meemeowairlines

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log

class DatabaseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val db = dbHelper.writableDatabase

    fun isEmailExists(email: String): Boolean {
        val columns = arrayOf(DatabaseHelper.COLUMN_EMAIL_ADDRESS)
        val selection = "${DatabaseHelper.COLUMN_EMAIL_ADDRESS} = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun insertUser(
        firstName: String, lastName: String, phoneNumber: String, emailAddress: String, password: String,
        nationality: String? = null, dateOfBirth: String? = null, placeOfBirth: String? = null, passportNumber: String? = null,
        gender: String? = null, emergency: String? = null
    ): Long {
        return try {
            val values = ContentValues().apply {
                put(DatabaseHelper.COLUMN_FIRST_NAME, firstName)
                put(DatabaseHelper.COLUMN_LAST_NAME, lastName)
                put(DatabaseHelper.COLUMN_PHONE_NUMBER, phoneNumber)
                put(DatabaseHelper.COLUMN_EMAIL_ADDRESS, emailAddress)
                put(DatabaseHelper.COLUMN_PASSWORD, password)
                put(DatabaseHelper.COLUMN_NATIONALITY, nationality)
                put(DatabaseHelper.COLUMN_DATE_OF_BIRTH, dateOfBirth)
                put(DatabaseHelper.COLUMN_PLACE_OF_BIRTH, placeOfBirth)
                put(DatabaseHelper.COLUMN_PASSPORT_NUMBER, passportNumber)
                put(DatabaseHelper.COLUMN_GENDER, gender)
                put(DatabaseHelper.COLUMN_EMERGENCY, emergency)
            }
            val result = db.insert(DatabaseHelper.TABLE_USERS, null, values)
            Log.d("DatabaseManager", "insertUser: result=$result")
            result
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error inserting user", e)
            -1
        }
    }

    fun insertBooking(
        userId: Long,  // New parameter
        bookingReference: String, ticketNumber: String, flightNumber: String, passengerName: String,
        time: String, departure: String, arrival: String, departureDate: String, arrivalDate: String?,
        flightClass: String, passengerCount: Int, totalPrice: Double
    ): Long {
        return try {
            val values = ContentValues().apply {
                put(DatabaseHelper.COLUMN_BOOKING_REFERENCE, bookingReference)
                put(DatabaseHelper.COLUMN_TICKET_NUMBER, ticketNumber)
                put(DatabaseHelper.COLUMN_FLIGHT_NUMBER, flightNumber)
                put(DatabaseHelper.COLUMN_PASSENGER_NAME, passengerName)
                put(DatabaseHelper.COLUMN_TIME, time)
                put(DatabaseHelper.COLUMN_DEPARTURE, departure)
                put(DatabaseHelper.COLUMN_ARRIVAL, arrival)
                put(DatabaseHelper.COLUMN_DEPARTURE_DATE, departureDate)
                put(DatabaseHelper.COLUMN_ARRIVAL_DATE, arrivalDate)
                put(DatabaseHelper.COLUMN_FLIGHT_CLASS, flightClass)
                put(DatabaseHelper.COLUMN_PASSENGER_COUNT, passengerCount)
                put(DatabaseHelper.COLUMN_TOTAL_PRICE, totalPrice)
                put(DatabaseHelper.COLUMN_CHECKIN_STATUS, "Processing")
                put(DatabaseHelper.COLUMN_USER_ID, userId)  // Insert userId
            }
            val result = db.insert(DatabaseHelper.TABLE_BOOKINGS, null, values)
            Log.d("DatabaseManager", "insertBooking: result=$result")
            result
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error inserting booking", e)
            -1
        }
    }

    fun getUserBookings(userId: Long): Cursor? {
        val columns = arrayOf(
            DatabaseHelper.COLUMN_BOOKING_REFERENCE, DatabaseHelper.COLUMN_TICKET_NUMBER,
            DatabaseHelper.COLUMN_FLIGHT_NUMBER, DatabaseHelper.COLUMN_PASSENGER_NAME,
            DatabaseHelper.COLUMN_TIME, DatabaseHelper.COLUMN_DEPARTURE, DatabaseHelper.COLUMN_ARRIVAL,
            DatabaseHelper.COLUMN_DEPARTURE_DATE, DatabaseHelper.COLUMN_ARRIVAL_DATE,
            DatabaseHelper.COLUMN_FLIGHT_CLASS, DatabaseHelper.COLUMN_PASSENGER_COUNT,
            DatabaseHelper.COLUMN_TOTAL_PRICE, DatabaseHelper.COLUMN_CHECKIN_STATUS
        )
        val selection = "${DatabaseHelper.COLUMN_USER_ID} = ?"
        val selectionArgs = arrayOf(userId.toString())
        return db.query(DatabaseHelper.TABLE_BOOKINGS, columns, selection, selectionArgs, null, null, null)
    }

    fun getUserByEmail(email: String): Cursor? {
        val columns = arrayOf(
            DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_FIRST_NAME, DatabaseHelper.COLUMN_LAST_NAME,
            DatabaseHelper.COLUMN_PHONE_NUMBER, DatabaseHelper.COLUMN_EMAIL_ADDRESS, DatabaseHelper.COLUMN_PASSWORD,
            DatabaseHelper.COLUMN_NATIONALITY, DatabaseHelper.COLUMN_DATE_OF_BIRTH, DatabaseHelper.COLUMN_PLACE_OF_BIRTH,
            DatabaseHelper.COLUMN_PASSPORT_NUMBER, DatabaseHelper.COLUMN_GENDER, DatabaseHelper.COLUMN_EMERGENCY
        )
        val selection = "${DatabaseHelper.COLUMN_EMAIL_ADDRESS} = ?"
        val selectionArgs = arrayOf(email)
        return db.query(
            DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null
        )
    }

    fun getUser(id: Long): Cursor? {
        val columns = arrayOf(
            DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_FIRST_NAME, DatabaseHelper.COLUMN_LAST_NAME,
            DatabaseHelper.COLUMN_PHONE_NUMBER, DatabaseHelper.COLUMN_EMAIL_ADDRESS, DatabaseHelper.COLUMN_PASSWORD,
            DatabaseHelper.COLUMN_NATIONALITY, DatabaseHelper.COLUMN_DATE_OF_BIRTH, DatabaseHelper.COLUMN_PLACE_OF_BIRTH,
            DatabaseHelper.COLUMN_PASSPORT_NUMBER, DatabaseHelper.COLUMN_GENDER, DatabaseHelper.COLUMN_EMERGENCY
        )
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.query(
            DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null
        )
    }

    fun updateUserProfile(
        id: Long,
        phoneNumber: String,
        nationality: String?,
        placeOfBirth: String?,
        passportNumber: String?,
        gender: String?,
        emergency: String?,
        dateOfBirth: String?
    ): Boolean {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PHONE_NUMBER, phoneNumber)
            put(DatabaseHelper.COLUMN_NATIONALITY, nationality)
            put(DatabaseHelper.COLUMN_PLACE_OF_BIRTH, placeOfBirth)
            put(DatabaseHelper.COLUMN_PASSPORT_NUMBER, passportNumber)
            put(DatabaseHelper.COLUMN_GENDER, gender)
            put(DatabaseHelper.COLUMN_EMERGENCY, emergency)
            put(DatabaseHelper.COLUMN_DATE_OF_BIRTH, dateOfBirth)
        }
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val rowsUpdated = db.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs)
        return rowsUpdated > 0
    }

    fun getLastBookingId(): Long {
        val query = "SELECT MAX(CAST(SUBSTR(${DatabaseHelper.COLUMN_BOOKING_REFERENCE}, 9) AS INTEGER)) FROM ${DatabaseHelper.TABLE_BOOKINGS}"
        val cursor = db.rawQuery(query, null)
        val lastId = if (cursor.moveToFirst()) cursor.getLong(0) else 0
        cursor.close()
        return lastId
    }

    fun getLastTicketId(): Long {
        val query = "SELECT MAX(CAST(SUBSTR(${DatabaseHelper.COLUMN_TICKET_NUMBER}, INSTR(${DatabaseHelper.COLUMN_TICKET_NUMBER}, '-') + 4) AS INTEGER)) FROM ${DatabaseHelper.TABLE_BOOKINGS}"
        val cursor = db.rawQuery(query, null)
        val lastId = if (cursor.moveToFirst()) cursor.getLong(0) else 0
        cursor.close()
        return lastId
    }

    fun isBookingReferenceValid(bookingReference: String, lastName: String): Pair<Boolean, Boolean> {
        val columns = arrayOf(DatabaseHelper.COLUMN_BOOKING_REFERENCE, DatabaseHelper.COLUMN_CHECKIN_STATUS)
        val selection = "${DatabaseHelper.COLUMN_BOOKING_REFERENCE} = ? AND LOWER(${DatabaseHelper.COLUMN_PASSENGER_NAME}) LIKE ?"
        val selectionArgs = arrayOf(bookingReference, "%${lastName.lowercase()}%")
        val cursor = db.query(DatabaseHelper.TABLE_BOOKINGS, columns, selection, selectionArgs, null, null, null)
        val isValid = cursor.count > 0
        var isCheckedIn = false
        if (cursor.moveToFirst()) {
            val checkInStatus = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CHECKIN_STATUS))
            isCheckedIn = checkInStatus == "Checked-In"
        }
        cursor.close()
        return Pair(isValid, isCheckedIn)
    }

    fun isTicketNumberValid(ticketNumber: String, lastName: String): Pair<Boolean, Boolean> {
        val columns = arrayOf(DatabaseHelper.COLUMN_TICKET_NUMBER, DatabaseHelper.COLUMN_CHECKIN_STATUS)
        val selection = "${DatabaseHelper.COLUMN_TICKET_NUMBER} = ? AND LOWER(${DatabaseHelper.COLUMN_PASSENGER_NAME}) LIKE ?"
        val selectionArgs = arrayOf(ticketNumber, "%${lastName.lowercase()}%")
        val cursor = db.query(DatabaseHelper.TABLE_BOOKINGS, columns, selection, selectionArgs, null, null, null)
        val isValid = cursor.count > 0
        var isCheckedIn = false
        if (cursor.moveToFirst()) {
            val checkInStatus = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CHECKIN_STATUS))
            isCheckedIn = checkInStatus == "Checked-In"
        }
        cursor.close()
        return Pair(isValid, isCheckedIn)
    }

    // Update check-in status
    fun updateCheckInStatus(identifier: String, status: String) {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CHECKIN_STATUS, status)
        }
        val selection = "${DatabaseHelper.COLUMN_BOOKING_REFERENCE} = ? OR ${DatabaseHelper.COLUMN_TICKET_NUMBER} = ?"
        val selectionArgs = arrayOf(identifier, identifier)
        db.update(DatabaseHelper.TABLE_BOOKINGS, values, selection, selectionArgs)
    }
}

