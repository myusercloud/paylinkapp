package com.harry.pay.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
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
import com.harry.pay.navigation.ROUT_EDIT
import com.harry.pay.navigation.ROUT_LOGIN
import com.harry.pay.navigation.ROUT_REGISTER

@Composable
fun ProfileScreen(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val userDao = UserDatabase.getDatabase(context).userDao()
    val userRepository = UserRepository(userDao)
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userRepository))

    val user by profileViewModel.userProfile.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        profileViewModel.loadUserProfile(userId)
    }

    if (user != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AnimatedVisibility(visible = true) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = if (user!!.profilePictureUri.isNullOrEmpty()) {
                                painterResource(id = R.drawable.ic_launcher_foreground)
                            } else {
                                rememberAsyncImagePainter(user!!.profilePictureUri)
                            },
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = user!!.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = user!!.email,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = "Phone", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(" ${user!!.phoneNumber}", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.business),
                                contentDescription = "Business Icon"
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(" ${user!!.businessName}", fontSize = 16.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                Button(
                    onClick = { navController.navigate(ROUT_EDIT) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate(ROUT_LOGIN) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D5553)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out", color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Profile")
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Profile") },
                text = { Text("Are you sure you want to permanently delete your profile? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            profileViewModel.deleteUserProfile(user!!.id)
                            showDeleteDialog = false
                            navController.navigate(ROUT_REGISTER) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
