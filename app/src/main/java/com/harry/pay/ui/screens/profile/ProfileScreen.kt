package com.harry.pay.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.harry.pay.R
import com.harry.pay.repository.UserRepository
import com.harry.pay.viewmodel.ProfileViewModel
import com.harry.pay.viewmodel.ProfileViewModelFactory
import com.harry.pay.data.UserDatabase

@Composable
fun ProfileScreen(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val userDao = UserDatabase.getDatabase(context).userDao()
    val userRepository = UserRepository(userDao)
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userRepository))

    val user by profileViewModel.userProfile.collectAsState()

    LaunchedEffect(userId) {
        profileViewModel.loadUserProfile(userId)
    }

    if (user != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(user?.profilePictureUri),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.name ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = user?.email ?: "",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Phone: ${user?.phoneNumber ?: "N/A"}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Address: ${user?.businessName ?: "N/A"}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("edit_profile_screen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("login_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Log Out", color = Color.White)
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
}
