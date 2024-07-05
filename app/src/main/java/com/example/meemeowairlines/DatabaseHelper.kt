package com.example.meemeowairlines

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MeemeowAirlines.db"
        private const val DATABASE_VERSION = 9 // Incremented version number
        const val TABLE_USERS = "users"

        // Column names
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_EMAIL_ADDRESS = "email_address"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_NATIONALITY = "nationality"
        const val COLUMN_AGE = "age"
        const val COLUMN_PLACE_OF_BIRTH = "place_of_birth"
        const val COLUMN_PASSPORT_NUMBER = "passport_number"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_EMERGENCY = "emergency"

        // Create table SQL query (new schema can be defined here if needed)
        private const val TABLE_CREATE = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FIRST_NAME TEXT NOT NULL,
                $COLUMN_LAST_NAME TEXT NOT NULL,
                $COLUMN_PHONE_NUMBER TEXT NOT NULL,
                $COLUMN_EMAIL_ADDRESS TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_NATIONALITY TEXT,
                $COLUMN_AGE INTEGER,
                $COLUMN_PLACE_OF_BIRTH TEXT,
                $COLUMN_PASSPORT_NUMBER TEXT,
                $COLUMN_GENDER TEXT,
                $COLUMN_EMERGENCY TEXT
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 9) {
            db.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_EMERGENCY TEXT")
        }
    }
}
