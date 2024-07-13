@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.meemeowairlines

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.meemeowairlines.ui.theme.YourAppTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("InvalidColorHexValue")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isLoggedIn = isUserLoggedIn(sharedPreferences)

        if (!isLoggedIn) {
            Log.d("MainActivity", "User not logged in, redirecting to LoginActivity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        } else {
            Log.d("MainActivity", "User is logged in")
        }

        // Set the color of the system status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        window.statusBarColor = 0xFF2C3446.toInt()

        setContent {
            YourAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFEEF)
                ) {
                    MainScreen()
                }
            }
        }
    }

    private fun isUserLoggedIn(sharedPreferences: SharedPreferences): Boolean {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)
        Log.d("MainActivity", "isUserLoggedIn: $isLoggedIn, rememberMe: $rememberMe")
        return isLoggedIn && rememberMe
    }
}


@Composable
fun MainScreen() {
    val context = LocalContext.current

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    HandleBackButton(backPressedDispatcher)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Meemeow Airlines",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier.padding(start = 7.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        IconButton(onClick = {
                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.profile_6),
                                contentDescription = "Profile Icon",
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
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.final_catlogo_removebg),
                    contentDescription = "Cat Logo",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(0.dp))
            Text(
                text = "Every trip without a guilt!",
                color = Color(0xFFF99063),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Dream into reality? Let's plan your next travel",
                color = Color(0xFFF99063),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(32.dp)) // Add some space before the button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, BookActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF99063)
                    ),
                    border = BorderStroke(2.dp, Color(0xFFF87943)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.padding(horizontal = 26.dp, vertical = 10.dp),
                    contentPadding = PaddingValues(horizontal = 34.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Book Now",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun HandleBackButton(backPressedDispatcher: OnBackPressedDispatcher?) {
    val context = LocalContext.current
    DisposableEffect(context) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Your custom back button behavior
                (context as? MainActivity)?.moveTaskToBack(true)
            }
        }
        backPressedDispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }
}

@Composable
fun BottomNavigationBar() {
    val context = LocalContext.current
    val items = listOf(
        BottomNavItem("home", "Home", R.drawable.home_1, MainActivity::class.java),
        BottomNavItem("manage_booking", "Manage Booking", R.drawable.pass_2, ManageActivity::class.java),
        BottomNavItem("book_flight", "Book Flight", R.drawable.plane_3, BookActivity::class.java),
        BottomNavItem("check_in", "Check-In", R.drawable.luggage_4, CheckInActivity::class.java),
        BottomNavItem("more", "More", R.drawable.menu_5, MoreActivity::class.java)
    )
    NavigationBar(
        containerColor = Color(0xFF2C3446)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                },
                selected = false,
                onClick = {
                    val intent = Intent(context, item.activityClass)
                    context.startActivity(intent)
                }
            )
        }
    }
}

data class BottomNavItem(val route: String, val title: String, val iconRes: Int, val activityClass: Class<*>)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YourAppTheme {
        MainScreen()
    }
}
