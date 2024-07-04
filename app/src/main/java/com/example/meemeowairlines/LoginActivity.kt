package com.example.meemeowairlines

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var dbManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbManager = DatabaseManager(this)

        val loginForm = findViewById<LinearLayout>(R.id.loginForm)
        val signupForm = findViewById<LinearLayout>(R.id.signupForm)

        findViewById<Button>(R.id.loginToggleButton).setOnClickListener {
            loginForm.visibility = View.VISIBLE
            signupForm.visibility = View.GONE
        }

        findViewById<Button>(R.id.signupToggleButton).setOnClickListener {
            loginForm.visibility = View.GONE
            signupForm.visibility = View.VISIBLE
        }

        findViewById<Button>(R.id.signupButton).setOnClickListener {
            handleSignup()
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            handleLogin()
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                Log.w("LoginActivity", "Google sign-in failed with resultCode: ${result.resultCode}")
            }
        }

        findViewById<Button>(R.id.googleLoginButton).setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun handleSignup() {
        val firstName = findViewById<EditText>(R.id.signupFirstName).text.toString()
        val lastName = findViewById<EditText>(R.id.signupLastName).text.toString()
        val phoneNumber = findViewById<EditText>(R.id.signupPhoneNumber).text.toString()
        val email = findViewById<EditText>(R.id.signupEmail).text.toString()
        val password = findViewById<EditText>(R.id.signupPassword).text.toString()
        val reenterPassword = findViewById<EditText>(R.id.signupReenterPassword).text.toString()
        val termsCheckBox = findViewById<CheckBox>(R.id.termsCheckBox)

        if (firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank() || email.isBlank() || password.isBlank() || reenterPassword.isBlank()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!termsCheckBox.isChecked) {
            Toast.makeText(this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != reenterPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbManager.isEmailExists(email)) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val userId = dbManager.insertUser(firstName, lastName, phoneNumber, email, password)
            if (userId > 0) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error creating account", e)
            Toast.makeText(this, "Error creating account: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLogin() {
        val email = findViewById<EditText>(R.id.loginEmail).text.toString()
        val password = findViewById<EditText>(R.id.loginPassword).text.toString()
        val rememberMe = findViewById<CheckBox>(R.id.rememberMeCheckBox).isChecked

        val cursor = dbManager.getUserByEmail(email)
        if (cursor != null && cursor.moveToFirst()) {
            val storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD))
            Log.d("LoginActivity", "Stored password: $storedPassword")
            Log.d("LoginActivity", "Entered password: $password")

            // Add a debug log to check if the passwords are compared correctly
            Log.d("LoginActivity", "Passwords match: ${password == storedPassword}")

            if (password == storedPassword) {
                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putBoolean("isLoggedIn", true)
                    putBoolean("rememberMe", rememberMe)
                    apply()
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
        cursor?.close()
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.d("LoginActivity", "signInResult:success, account: $account")

            if (account != null) {
                val firstName = account.givenName ?: ""
                val lastName = account.familyName ?: ""
                val email = account.email ?: ""

                val randomPassword = generateRandomPassword()

                val userId = dbManager.insertUser(firstName, lastName, "", email, randomPassword)

                if (userId > 0) {
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putBoolean("isLoggedIn", true)
                        apply()
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("LoginActivity", "Failed to insert user into database")
                    Toast.makeText(this, "Failed to insert user into database", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("LoginActivity", "Google Sign-In account is null")
                Toast.makeText(this, "Google Sign-In account is null", Toast.LENGTH_SHORT).show()
            }

        } catch (e: ApiException) {
            Log.w("LoginActivity", "signInResult:failed code=" + e.statusCode, e)
            Toast.makeText(this, "Sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateRandomPassword(): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..14)
            .map { chars.random() }
            .joinToString("")
    }
}
