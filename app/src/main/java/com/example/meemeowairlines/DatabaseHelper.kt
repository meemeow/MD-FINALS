package com.example.meemeowairlines

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MeemeowAirlines.db"
        private const val DATABASE_VERSION = 7 // Incremented version number
        const val TABLE_USERS = "users"

        // Column names
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_EMAIL_ADDRESS = "email_address"
        const val COLUMN_PASSWORD = "password"

        // Create table SQL query (new schema can be defined here if needed)
        private const val TABLE_CREATE = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FIRST_NAME TEXT NOT NULL,
                $COLUMN_LAST_NAME TEXT NOT NULL,
                $COLUMN_PHONE_NUMBER TEXT NOT NULL,
                $COLUMN_EMAIL_ADDRESS TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS") // Drop the existing table
        onCreate(db) // Create the new table
    }
}
