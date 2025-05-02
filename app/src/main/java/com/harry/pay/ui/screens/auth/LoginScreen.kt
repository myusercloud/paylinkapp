package com.harry.pay.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.harry.pay.R
import com.harry.pay.ui.theme.FintechLightBackground
import com.harry.pay.ui.theme.FintechTeal

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    navController: NavHostController,
    onSuccess: () -> Unit,
    loginSuccess: Boolean,
    error: String?,
    isLoading: Boolean
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Navigate automatically after successful login
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) onSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FintechLightBackground)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Optional Logo + Title
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )

        Text("Welcome Back", style = MaterialTheme.typography.titleLarge, color = FintechTeal)

        Spacer(modifier = Modifier.height(24.dp))

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FintechTeal,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FintechTeal,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Show error if any
        if (!error.isNullOrBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Login button
        Button(
            onClick = {
                val trimmedUsername = username.trim()
                val trimmedPassword = password.trim()
                if (trimmedUsername.isNotBlank() && trimmedPassword.isNotBlank()) {
                    println("Login button clicked with $username / $password")
                    onLogin(trimmedUsername, trimmedPassword)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FintechTeal),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text("Login", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation to Register
        TextButton(onClick = {
            navController.navigate("sign_up_screen")
        }) {
            Text("Don't have an account? Sign up", color = FintechTeal)
        }
    }
}
