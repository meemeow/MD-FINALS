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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.currentBackStackEntryAsState
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
                    val navController = rememberNavController()
                    MainScreen(navController = navController)
                }
            }
        }
    }

    private fun isUserLoggedIn(sharedPreferences: SharedPreferences): Boolean {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)
        Log.d("MainActivity", "isLoggedIn: $isLoggedIn, rememberMe: $rememberMe")
        return isLoggedIn && rememberMe
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    HandleBackButton(backPressedDispatcher)

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = if (currentRoute == "home") 56.dp else 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (currentRoute) {
                                "home" -> "Meemeow Airlines"
                                "manage_booking" -> "Manage Booking"
                                "book_flight" -> "Book Flight"
                                "check_in" -> "Check-In"
                                "more" -> "More"
                                else -> "Meemeow Airlines"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    if (currentRoute == "home") {
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
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF99063)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController = navController) }
            composable("manage_booking") { ManageScreen() }
            composable("book_flight") { BookScreen() }
            composable("check_in") { CheckInScreen() }
            composable("more") { MoreScreen() }
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
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", "Home", R.drawable.home_1),
        BottomNavItem("manage_booking", "Manage Booking", R.drawable.pass_2),
        BottomNavItem("book_flight", "Book Flight", R.drawable.plane_3),
        BottomNavItem("check_in", "Check-In", R.drawable.luggage_4),
        BottomNavItem("more", "More", R.drawable.menu_5)
    )

    NavigationBar(
        containerColor = Color(0xFF2C3446)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) Color(0xFFF87943) else Color.White
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (isSelected) Color(0xFFF87943) else Color.White
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent, // Set the indicator color to transparent
                    selectedIconColor = Color(0xFFF99063),
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color(0xFFF99063),
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}


data class BottomNavItem(val route: String, val title: String, val iconRes: Int)

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFEEF))
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.final_catlogo_removebg),
            contentDescription = "Cat Logo",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
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
        Button(
            onClick = {
                navController.navigate("book_flight") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
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

@Composable
fun ManageScreen() {
    // Content for Manage Booking screen
}

@Composable
fun BookScreen() {
    // Content for Book Flight screen
}

@Composable
fun CheckInScreen() {
    // Content for Check-In screen
}

@Composable
fun MoreScreen() {
    val context = LocalContext.current

    // Grid Layout for Cards
    val items = listOf(
        Triple("Boarding Pass", R.drawable.pass_14, BoardingPassActivity::class.java),
        Triple("About Covid-19", R.drawable.covid_8, CovidInfoActivity::class.java),
        Triple("Flight Status", R.drawable.planeinfo_9, FlightStatusActivity::class.java),
        Triple("Flight Disruption", R.drawable.planewarn_10, FlightDisruptionActivity::class.java),
        Triple("Contact Us", R.drawable.contact_11, ContactUsActivity::class.java),
        Triple("Terms &\nConditions", R.drawable.terms_12, TermsActivity::class.java), // Note the line break
        Triple("Settings", R.drawable.settings_13, SettingsActivity::class.java)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFEEF))
            .padding(16.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items.size) { index ->
            val item = items[index]
            MoreCard(
                title = item.first,
                iconRes = item.second,
                widthDp = 150.dp, // Set the desired width here
                heightDp = 150.dp, // Set the desired height here
                onClick = {
                    val intent = Intent(context, item.third)
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun MoreCard(title: String, iconRes: Int, widthDp: Dp, heightDp: Dp, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(widthDp)
            .height(heightDp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(10.dp) // Match corner radius from XML
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp) // Match corner radius from XML
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp) // Match corner radius from XML
                )
                .padding(top = if (title.contains("Terms &\nConditions")) 12.dp else 0.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "$title Icon",
                modifier = Modifier.size(38.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color(0xFFF99063),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    YourAppTheme {
        MainScreen(navController = navController)
    }
}
