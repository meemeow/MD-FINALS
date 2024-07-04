package com.example.meemeowairlines

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log

class DatabaseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val db = dbHelper.writableDatabase

    fun insertUser(
        firstName: String, lastName: String, phoneNumber: String, emailAddress: String, password: String
    ): Long {
        return try {
            val values = ContentValues().apply {
                put(DatabaseHelper.COLUMN_FIRST_NAME, firstName)
                put(DatabaseHelper.COLUMN_LAST_NAME, lastName)
                put(DatabaseHelper.COLUMN_PHONE_NUMBER, phoneNumber)
                put(DatabaseHelper.COLUMN_EMAIL_ADDRESS, emailAddress)
                put(DatabaseHelper.COLUMN_PASSWORD, password)
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
            DatabaseHelper.COLUMN_PHONE_NUMBER, DatabaseHelper.COLUMN_EMAIL_ADDRESS, DatabaseHelper.COLUMN_PASSWORD
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
            DatabaseHelper.COLUMN_PHONE_NUMBER, DatabaseHelper.COLUMN_EMAIL_ADDRESS, DatabaseHelper.COLUMN_PASSWORD
        )
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.query(
            DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null
        )
    }

    fun updateUser(
        id: Long, firstName: String, lastName: String, phoneNumber: String, emailAddress: String, password: String
    ): Int {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_FIRST_NAME, firstName)
            put(DatabaseHelper.COLUMN_LAST_NAME, lastName)
            put(DatabaseHelper.COLUMN_PHONE_NUMBER, phoneNumber)
            put(DatabaseHelper.COLUMN_EMAIL_ADDRESS, emailAddress)
            put(DatabaseHelper.COLUMN_PASSWORD, password)
        }
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs)
    }

    fun deleteUser(id: Long): Int {
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(DatabaseHelper.TABLE_USERS, selection, selectionArgs)
    }

    fun isEmailExists(email: String): Boolean {
        val columns = arrayOf(DatabaseHelper.COLUMN_EMAIL_ADDRESS)
        val selection = "${DatabaseHelper.COLUMN_EMAIL_ADDRESS} = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
