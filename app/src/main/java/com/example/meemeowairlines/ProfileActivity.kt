package com.example.meemeowairlines

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.edit

class ProfileActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        if (!isUserLoggedIn()) {
            Log.d("ProfileActivity", "User not logged in, redirecting to LoginActivity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        } else {
            Log.d("ProfileActivity", "User is logged in")
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            handleLogout()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        Log.d("ProfileActivity", "isUserLoggedIn: $isLoggedIn")
        return isLoggedIn
    }

    private fun handleLogout() {
        sharedPreferences.edit {
            putBoolean("isLoggedIn", false)
            apply()
        }

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
