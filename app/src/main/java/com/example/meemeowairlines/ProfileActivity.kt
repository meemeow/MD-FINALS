@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.meemeowairlines

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meemeowairlines.ui.theme.YourAppTheme
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbManager: DatabaseManager
    private var userId: Long = -1

    private lateinit var editProfileButton: Button
    private lateinit var logoutButton: Button
    private lateinit var phoneNumberEditText: EditText
    private lateinit var nationalityEditText: EditText
    private lateinit var dateOfBirthEditText: TextView
    private lateinit var placeOfBirthEditText: EditText
    private lateinit var passportNumberEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var emergencyEditText: EditText
    private lateinit var firstNameTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var nationalityTextView: TextView
    private lateinit var dateOfBirthTextView: TextView
    private lateinit var placeOfBirthTextView: TextView
    private lateinit var passportNumberTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var emergencyTextView: TextView
    private val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    private var isEditMode = false
    private var isDateOfBirthEditable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        dbManager = DatabaseManager(this)

        if (!isUserLoggedIn()) {
            Log.d("ProfileActivity", "User not logged in, redirecting to LoginActivity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        } else {
            Log.d("ProfileActivity", "User is logged in")
        }

        userId = sharedPreferences.getLong("userId", -1)
        if (userId == -1L) {
            handleLogout()
            return
        }

        initializeViews()
        loadUserData()

        editProfileButton.setOnClickListener {
            if (isEditMode) {
                saveProfileChanges()
            } else {
                enableEditing()
            }
        }

        logoutButton.setOnClickListener {
            handleLogout()
        }

        // Set the content of the ComposeView
        val composeView = findViewById<ComposeView>(R.id.composeViewTopBar)
        composeView.setContent {
            YourAppTheme {
                ProfileTopBar()
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        userId = sharedPreferences.getLong("userId", -1)
        return userId != -1L
    }

    private fun initializeViews() {
        editProfileButton = findViewById(R.id.editButton)
        logoutButton = findViewById(R.id.logoutButton)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        nationalityEditText = findViewById(R.id.nationalityEditText)
        dateOfBirthEditText = findViewById(R.id.dateOfBirthEditText)
        placeOfBirthEditText = findViewById(R.id.placeOfBirthEditText)
        passportNumberEditText = findViewById(R.id.passportNumberEditText)
        genderEditText = findViewById(R.id.genderEditText)
        emergencyEditText = findViewById(R.id.emergencyEditText)
        firstNameTextView = findViewById(R.id.firstNameTextView)
        lastNameTextView = findViewById(R.id.lastNameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)
        nationalityTextView = findViewById(R.id.nationalityTextView)
        dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView)
        placeOfBirthTextView = findViewById(R.id.placeOfBirthTextView)
        passportNumberTextView = findViewById(R.id.passportNumberTextView)
        genderTextView = findViewById(R.id.genderTextView)
        emergencyTextView = findViewById(R.id.emergencyTextView)

        disableEditing()

        dateOfBirthEditText.setOnClickListener {
            if (isDateOfBirthEditable) {
                showDatePickerDialog()
            }
        }
    }

    private fun loadUserData() {
        val cursor: Cursor? = dbManager.getUser(userId)
        cursor?.moveToFirst()
        if (cursor != null && cursor.count > 0) {
            firstNameTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FIRST_NAME))
            lastNameTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LAST_NAME))
            emailTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL_ADDRESS))
            phoneNumberEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE_NUMBER)))
            nationalityEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NATIONALITY)))
            placeOfBirthEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLACE_OF_BIRTH)))
            passportNumberEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSPORT_NUMBER)))
            genderEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENDER)))
            emergencyEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMERGENCY)))

            // Set the date of birth from the database
            val dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_OF_BIRTH))
            if (!dateOfBirth.isNullOrEmpty()) {
                dateOfBirthEditText.text = dateOfBirth
                isDateOfBirthEditable = false
            }

            cursor.close()
        }
    }

    private fun enableEditing() {
        phoneNumberEditText.isEnabled = true
        nationalityEditText.isEnabled = true
        placeOfBirthEditText.isEnabled = true
        passportNumberEditText.isEnabled = true
        genderEditText.isEnabled = true
        emergencyEditText.isEnabled = true

        editProfileButton.text = "Save"
        isEditMode = true
    }

    private fun disableEditing() {
        phoneNumberEditText.isEnabled = false
        nationalityEditText.isEnabled = false
        placeOfBirthEditText.isEnabled = false
        passportNumberEditText.isEnabled = false
        genderEditText.isEnabled = false
        emergencyEditText.isEnabled = false

        phoneNumberEditText.setBackgroundResource(android.R.color.transparent)
        nationalityEditText.setBackgroundResource(android.R.color.transparent)
        placeOfBirthEditText.setBackgroundResource(android.R.color.transparent)
        passportNumberEditText.setBackgroundResource(android.R.color.transparent)
        genderEditText.setBackgroundResource(android.R.color.transparent)
        emergencyEditText.setBackgroundResource(android.R.color.transparent)

        editProfileButton.text = "Edit Profile"
        isEditMode = false
    }

    private fun saveProfileChanges() {
        val phoneNumber = phoneNumberEditText.text.toString()
        val nationality = nationalityEditText.text.toString()
        val placeOfBirth = placeOfBirthEditText.text.toString()
        val passportNumber = passportNumberEditText.text.toString()
        val gender = genderEditText.text.toString()
        val emergency = emergencyEditText.text.toString()

        val success = dbManager.updateUserProfile(
            userId, phoneNumber, nationality, placeOfBirth, passportNumber, gender, emergency
        )

        if (success) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
        }

        disableEditing()
    }

    
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            dateOfBirthEditText.text = dateFormatter.format(selectedDate.time)
            isDateOfBirthEditable = false // Make it non-editable after the first date is selected
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun handleLogout() {
        sharedPreferences.edit().remove("userId").apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun ProfileTopBar() {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profile",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier.padding(start = 7.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_7),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF99063)
        )
    )
}