@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.meemeowairlines

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.border
import androidx.compose.material3.*
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.meemeowairlines.ui.theme.YourAppTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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
    var tripType by remember { mutableStateOf("Round Trip") }
    val airports = listOf(
        "Manila (MNL)",
        "Cebu (CEB)",
        "Angeles City (CRK)",
        "Davao City (DVO)",
        "Iloilo City (ILO)",
        "Kalibo (KLO)",
        "Puerto Princesa (PPS)",
        "Zamboanga City (ZAM)",
        "General Santos City (GES)",
        "Bacolod City (BCD)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFEEF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(
                onClick = { tripType = "Round Trip" },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (tripType == "Round Trip") Color(0xFFF99063) else Color.Transparent,
                    contentColor = if (tripType == "Round Trip") Color.White else Color(0xFFF99063)
                )
            ) {
                Text("Round Trip")
            }
            TextButton(
                onClick = { tripType = "One Way" },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (tripType == "One Way") Color(0xFFF99063) else Color.Transparent,
                    contentColor = if (tripType == "One Way") Color.White else Color(0xFFF99063)
                )
            ) {
                Text("One Way")
            }
        }

        if (tripType == "Round Trip") {
            RoundTripForm(airports)
        } else {
            OneWayForm(airports)
        }
    }
}

@Composable
fun RoundTripForm(airports: List<String>) {
    var departure by remember { mutableStateOf("") }
    var arrival by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    val ageGroups = remember {
        mutableStateOf(
            mapOf(
                "Child (2-11)" to 0,
                "Adult (12-59)" to 0,
                "Senior (60+)" to 0
            )
        )
    }
    var flightClass by remember { mutableStateOf("Economy") }

    val context = LocalContext.current

    FlightForm(
        airports,
        departure,
        arrival,
        startDate,
        endDate,
        ageGroups,
        flightClass,
        onDepartureChange = { departure = it },
        onArrivalChange = { arrival = it },
        onTravelDatesChange = null,
        onTravelDateChange = null,
        onFlightClassChange = { flightClass = it }
    ) {
        DateRangePickerDialog(
            context = context,
            startDate = startDate,
            endDate = endDate,
            onStartDateSelected = { date -> startDate = date },
            onEndDateSelected = { date -> endDate = date }
        )
    }

    Button(
        onClick = {
            if (departure.isEmpty() || arrival.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || ageGroups.value.values.sum() == 0) {
                Toast.makeText(context, "Please fill all fields and select at least one passenger.", Toast.LENGTH_SHORT).show()
            } else if (SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDate)!!
                    .after(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDate)!!)
            ) {
                Toast.makeText(context, "Departure date cannot be later than arrival date.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(context, SearchActivity::class.java).apply {
                    putExtra("departure", departure)
                    putExtra("arrival", arrival)
                    putExtra("departureDate", startDate)
                    putExtra("arrivalDate", endDate)
                    putExtra("flightClass", flightClass)
                    putExtra("tripType", "Round Trip")
                }
                context.startActivity(intent)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Search")
    }
}


@Composable
fun OneWayForm(airports: List<String>) {
    var departure by remember { mutableStateOf("") }
    var arrival by remember { mutableStateOf("") }
    var travelDate by remember { mutableStateOf("") }
    val ageGroups = remember {
        mutableStateOf(
            mapOf(
                "Child (2-11)" to 0,
                "Adult (12-59)" to 0,
                "Senior (60+)" to 0
            )
        )
    }
    var flightClass by remember { mutableStateOf("Economy") }

    val context = LocalContext.current

    FlightForm(
        airports,
        departure,
        arrival,
        travelDate,
        null,
        ageGroups,
        flightClass,
        onDepartureChange = { departure = it },
        onArrivalChange = { arrival = it },
        onTravelDatesChange = null,
        onTravelDateChange = { date -> travelDate = date },
        onFlightClassChange = { flightClass = it }
    ) {
        DatePickerDialog(
            context = context,
            selectedDate = travelDate,
            onDateSelected = { date ->
                travelDate = date
            }
        )
    }

    Button(
        onClick = {
            if (departure.isEmpty() || arrival.isEmpty() || travelDate.isEmpty() || ageGroups.value.values.sum() == 0) {
                Toast.makeText(context, "Please fill all fields and select at least one passenger.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(context, SearchActivity::class.java).apply {
                    putExtra("departure", departure)
                    putExtra("arrival", arrival)
                    putExtra("departureDate", travelDate)
                    putExtra("arrivalDate", "")
                    putExtra("flightClass", flightClass)
                    putExtra("tripType", "One Way")
                }
                context.startActivity(intent)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Search")
    }
}

@Composable
fun FlightForm(
    airports: List<String>,
    departure: String,
    arrival: String,
    departureDate: String?,
    arrivalDate: String?,
    ageGroups: MutableState<Map<String, Int>>,
    flightClass: String,
    onDepartureChange: (String) -> Unit,
    onArrivalChange: (String) -> Unit,
    onTravelDatesChange: ((Pair<String, String>) -> Unit)?,
    onTravelDateChange: ((String) -> Unit)?,
    onFlightClassChange: (String) -> Unit,
    datePickerContent: @Composable () -> Unit
) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        DropdownMenu(
            label = "Departure",
            options = airports,
            selectedOption = departure,
            onOptionSelected = onDepartureChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(
            label = "Arrival",
            options = airports.filter { it != departure },
            selectedOption = arrival,
            onOptionSelected = onArrivalChange
        )
        Spacer(modifier = Modifier.height(8.dp))

        datePickerContent()

        Spacer(modifier = Modifier.height(8.dp))
        PassengerCounter(ageGroups)
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(
            label = "Class",
            options = listOf("Economy", "Business", "First Class"),
            selectedOption = flightClass,
            onOptionSelected = onFlightClassChange
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DatePickerDialog(
    context: Context,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val oneMonthLater = calendar.apply { add(Calendar.MONTH, 1) }.time

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                val selected = calendar.time
                if (selected.after(oneMonthLater)) {
                    onDateSelected(dateFormat.format(selected))
                } else {
                    Toast.makeText(context, "Date must be after one month from now.", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,  // Set initial month to one month later
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = oneMonthLater.time
        }
    }

    Text(text = "Departure Date", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFF99063))
    Text(
        text = selectedDate.ifEmpty { "Select Date" },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(8.dp)
            .clickable {
                datePickerDialog.show()
            },
        color = if (selectedDate.isEmpty()) Color.Gray else Color.Black
    )
}

@Composable
fun DateRangePickerDialog(
    context: Context,
    startDate: String,
    endDate: String,
    onStartDateSelected: (String) -> Unit,
    onEndDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val oneMonthLater = calendar.apply { add(Calendar.MONTH, 1) }.time

    val startDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                val selected = calendar.time
                if (selected.after(oneMonthLater)) {
                    onStartDateSelected(dateFormat.format(selected))
                } else {
                    Toast.makeText(context, "Date must be after one month from now.", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = oneMonthLater.time
        }
    }

    val endDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                val selected = calendar.time
                if (selected.after(oneMonthLater)) {
                    if (startDate.isNotEmpty() && selected.before(dateFormat.parse(startDate))) {
                        Toast.makeText(context, "Arrival date must be after departure date.", Toast.LENGTH_SHORT).show()
                    } else {
                        onEndDateSelected(dateFormat.format(selected))
                    }
                } else {
                    Toast.makeText(context, "Date must be after one month from now.", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = oneMonthLater.time
        }
    }

    Column {
        Text(text = "Departure Date", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFF99063))
        Text(
            text = startDate.ifEmpty { "Select Date" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
                .clickable {
                    startDatePickerDialog.show()
                },
            color = if (startDate.isEmpty()) Color.Gray else Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Arrival Date", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFF99063))
        Text(
            text = endDate.ifEmpty { "Select Date" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
                .clickable {
                    endDatePickerDialog.show()
                },
            color = if (endDate.isEmpty()) Color.Gray else Color.Black
        )
    }
}

@Composable
fun DropdownMenu(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFF99063))
        Box {
            Text(
                text = selectedOption.ifEmpty { "Select $label" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { expanded = true }
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(8.dp),
                color = if (selectedOption.isEmpty()) Color.Gray else Color.Black
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuItem(onClick: () -> Unit, interactionSource: @Composable () -> Unit) {
    val interactionSourceInstance = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(width = 295.dp, height = 45.dp)
            .clickable(interactionSource = interactionSourceInstance, indication = null) {
                onClick()
            }
            .padding(8.dp)
    )
    {
        interactionSource()
    }
}

@Composable
fun PassengerCounter(ageGroups: MutableState<Map<String, Int>>) {
    val context = LocalContext.current

    Column {
        Text(text = "Passengers", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFF99063))
        ageGroups.value.keys.forEach { ageGroup ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(ageGroup, modifier = Modifier.weight(1f), color = Color(0xFFF99063))
                IconButton(onClick = {
                    val currentCount = ageGroups.value.values.sum()
                    if (currentCount < 9) {
                        val updatedCount = (ageGroups.value[ageGroup] ?: 0) + 1
                        ageGroups.value = ageGroups.value.toMutableMap().apply {
                            this[ageGroup] = updatedCount
                        }
                    } else {
                        Toast.makeText(context, "Maximum 9 passengers allowed.", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color(0xFFF99063))
                }
                Text("${ageGroups.value[ageGroup]}", color = Color(0xFFF99063))
                IconButton(onClick = {
                    val updatedCount = (ageGroups.value[ageGroup] ?: 0) - 1
                    if (updatedCount >= 0) {
                        ageGroups.value = ageGroups.value.toMutableMap().apply {
                            this[ageGroup] = updatedCount
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFF99063))
                }
            }
        }
    }
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
