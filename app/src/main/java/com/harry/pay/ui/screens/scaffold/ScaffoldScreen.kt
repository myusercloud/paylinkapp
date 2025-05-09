package com.harry.pay.ui.screens.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.harry.pay.model.User
import com.harry.pay.model.PaymentLink
import com.harry.pay.ui.screens.community.CommunityCirclesScreen
import com.harry.pay.ui.screens.link.CreateLinkScreen
import com.harry.pay.ui.screens.home.HomeScreen
import com.harry.pay.ui.screens.profile.ProfileScreen

@Composable
fun ScaffoldScreen(navController: NavController, currentUser: User, paymentLinks: List<PaymentLink>) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_link") }, // Navigate to the Create Link screen
                containerColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Gray,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Create") },
                    label = { Text("Create") },
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Menu") },
                    label = { Text("Menu") },
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedIndex == 3,
                    onClick = { selectedIndex = 3 }
                )
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                when (selectedIndex) {
                    0 -> HomeScreen(navController = navController, currentUser = currentUser, paymentLinks = paymentLinks)
                    1 -> CreateLinkScreen(
                        navController = navController,
                        onCreate = { paymentLink ->
                            // Handle the PaymentLink object, e.g.:
                            println("Created link: $paymentLink")
                            // Or maybe navigate or save it somewhere
                        }
                    )
                    2 -> CommunityCirclesScreen(navController = navController)
                    3 -> ProfileScreen(navController = navController, userId = 1)
                }
            }
        }
    )
}

@Composable
fun CenteredText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun FilterChip(text: String) {
    AssistChip(
        onClick = { /* TODO: Filter links */ },
        label = { Text(text) }
    )
}

@Composable
fun LinkItem(name: String, description: String, amount: String, date: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, fontWeight = FontWeight.Bold)
            Text(text = description)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = amount, fontWeight = FontWeight.SemiBold)
                Text(text = date)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScaffoldScreenPreview() {
    val dummyUser = User(
        id = 1,
        name = "Harry",
        email = "preview@example.com",
        phoneNumber = "1234567890",
        password = "password",
        businessName = "Preview Co",
        profilePictureUri = ""
    )

    val dummyLinks = listOf(
        PaymentLink(id = 1, title = "Link1", description = "Test 1", amount = 100.0, link = "https://paylink.com/1"),
        PaymentLink(id = 2, title = "Link2", description = "Test 2", amount = 200.0, link = "https://paylink.com/2")
    )

    ScaffoldScreen(navController = rememberNavController(), currentUser = dummyUser, paymentLinks = dummyLinks)
}
