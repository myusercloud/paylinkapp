package com.harry.pay.ui.screens.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ScaffoldScreen(navController: NavController) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Example action: Maybe open Add Payment screen */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favourites") },
                    label = { Text("Favourites") },
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Menu") },
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
            // Content body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (selectedIndex) {
                        0 -> "Welcome to Home"
                        1 -> "Here are your Favourites"
                        2 -> "This is the Menu"
                        3 -> "User Profile"
                        else -> ""
                    },
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ScaffoldScreenPreview() {
    ScaffoldScreen(navController = rememberNavController())
}
