package com.example.meemeowairlines

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MeemeowAirlines.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val TABLE_BOOKINGS = "bookings"

        // User table columns
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_EMAIL_ADDRESS = "email_address"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_NATIONALITY = "nationality"
        const val COLUMN_DATE_OF_BIRTH = "date_of_birth"
        const val COLUMN_PLACE_OF_BIRTH = "place_of_birth"
        const val COLUMN_PASSPORT_NUMBER = "passport_number"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_EMERGENCY = "emergency"

        // Booking table columns
        const val COLUMN_BOOKING_REFERENCE = "booking_reference"
        const val COLUMN_TICKET_NUMBER = "ticket_number"
        const val COLUMN_FLIGHT_NUMBER = "flight_number"
        const val COLUMN_PASSENGER_NAME = "passenger_name"
        const val COLUMN_TIME = "time"
        const val COLUMN_DEPARTURE = "departure"
        const val COLUMN_ARRIVAL = "arrival"
        const val COLUMN_DEPARTURE_DATE = "departure_date"
        const val COLUMN_ARRIVAL_DATE = "arrival_date"
        const val COLUMN_FLIGHT_CLASS = "flight_class"
        const val COLUMN_PASSENGER_COUNT = "passenger_count"
        const val COLUMN_TOTAL_PRICE = "total_price"
        const val COLUMN_CHECKIN_STATUS = "checkin_status"
        const val COLUMN_USER_ID = "user_id"  // New column
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_PHONE_NUMBER + " TEXT,"
                + COLUMN_EMAIL_ADDRESS + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_NATIONALITY + " TEXT,"
                + COLUMN_DATE_OF_BIRTH + " TEXT,"
                + COLUMN_PLACE_OF_BIRTH + " TEXT,"
                + COLUMN_PASSPORT_NUMBER + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_EMERGENCY + " TEXT" + ")")
        db.execSQL(createUsersTable)

        val createBookingsTable = ("CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_BOOKING_REFERENCE + " TEXT PRIMARY KEY,"
                + COLUMN_TICKET_NUMBER + " TEXT,"
                + COLUMN_FLIGHT_NUMBER + " TEXT,"
                + COLUMN_PASSENGER_NAME + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_DEPARTURE + " TEXT,"
                + COLUMN_ARRIVAL + " TEXT,"
                + COLUMN_DEPARTURE_DATE + " TEXT,"
                + COLUMN_ARRIVAL_DATE + " TEXT,"
                + COLUMN_FLIGHT_CLASS + " TEXT,"
                + COLUMN_PASSENGER_COUNT + " INTEGER,"
                + COLUMN_TOTAL_PRICE + " REAL,"
                + COLUMN_CHECKIN_STATUS + " TEXT DEFAULT 'Processing',"
                + COLUMN_USER_ID + " INTEGER,"
                + "FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID))")
        db.execSQL(createBookingsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKINGS")
        onCreate(db)
    }
}
