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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.harry.pay.R
import com.harry.pay.navigation.ROUT_HOME
import com.harry.pay.navigation.ROUT_LOGIN
import com.harry.pay.ui.theme.FintechTeal
import com.harry.pay.ui.theme.FintechLightBackground

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    navController: NavHostController,
    onSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FintechLightBackground),  // Set background color
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo
        Image(
            painter = painterResource(id = R.drawable.img), // Replace with your actual logo resource
            contentDescription = "App Logo",
            modifier = Modifier
                .size(110.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Welcome to PayLink",
            style = MaterialTheme.typography.headlineMedium,
            color = FintechTeal,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Username field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FintechTeal,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = FintechTeal,
                unfocusedLabelColor = Color.Gray
            )
        )

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FintechTeal,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = FintechTeal,
                unfocusedLabelColor = Color.Gray
            )
        )

        // Error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Login button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in both fields."
                    } else {
                        isLoading = true
                        errorMessage = ""
                        onLogin(username, password)
                        isLoading = false
                        onSuccess()
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
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign up link
        TextButton(onClick = {
            navController.navigate("sign_up_screen")
        }) {
            Text(
                text = "Don't have an account? Sign up",
                color = FintechTeal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(
        onLogin = { _, _ -> },
        navController = navController,
        onSuccess = {
            navController.navigate(ROUT_HOME) {
                popUpTo(ROUT_LOGIN) { inclusive = true }
            }
        }
    )
}
