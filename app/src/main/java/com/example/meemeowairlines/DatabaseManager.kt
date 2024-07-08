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
        emergency: String?
    ): Boolean {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PHONE_NUMBER, phoneNumber)
            put(DatabaseHelper.COLUMN_NATIONALITY, nationality)
            put(DatabaseHelper.COLUMN_PLACE_OF_BIRTH, placeOfBirth)
            put(DatabaseHelper.COLUMN_PASSPORT_NUMBER, passportNumber)
            put(DatabaseHelper.COLUMN_GENDER, gender)
            put(DatabaseHelper.COLUMN_EMERGENCY, emergency)
        }
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val rowsUpdated = db.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs)
        return rowsUpdated > 0
    }

    fun deleteUser(id: Long): Int {
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(DatabaseHelper.TABLE_USERS, selection, selectionArgs)
    }
}
