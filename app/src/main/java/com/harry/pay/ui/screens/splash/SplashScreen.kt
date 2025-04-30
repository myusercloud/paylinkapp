package com.harry.pay.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.harry.pay.R
import com.harry.pay.navigation.ROUT_REGISTER

@Composable
fun PayLinkSplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)  // Show splash for 2 seconds
        // Navigate to the next screen
        navController.navigate(ROUT_REGISTER)  // Replace with the actual screen route
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF06DCBE), Color(0xFF013430)) // Fintech teal gradient
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.img), // your custom icon
                contentDescription = "PayLink Logo",
                modifier = Modifier
                    .size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PayLink",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Link. Send. Get Paid.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PayLinkSplashScreenPreview() {
    // Using a dummy NavController for preview purposes
    val navController = rememberNavController()

    // Since we just want to preview the splash screen, we don't actually need to navigate anywhere
    PayLinkSplashScreen(navController = navController)
}
