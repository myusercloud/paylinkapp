package com.harry.pay.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.harry.pay.R
import com.harry.pay.repository.UserRepository
import com.harry.pay.viewmodel.ProfileViewModel
import com.harry.pay.viewmodel.ProfileViewModelFactory
import com.harry.pay.data.UserDatabase
import com.harry.pay.navigation.ROUT_EDIT
import com.harry.pay.navigation.ROUT_LOGIN
import com.harry.pay.navigation.ROUT_REGISTER
import com.harry.pay.ui.theme.FintechIconTint
import com.harry.pay.ui.theme.FintechLightBackground
import com.harry.pay.ui.theme.FintechTeal


@Composable
fun ProfileScreen(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val userDao = UserDatabase.getDatabase(context).userDao()
    val userRepository = UserRepository(userDao)
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userRepository))

    val user by profileViewModel.userProfile.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expandedMenu by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        profileViewModel.loadUserProfile(userId)
    }

    if (user != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Paylink | Profile screen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                color = FintechTeal
            )

            // Settings menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { expandedMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Settings", tint = FintechIconTint)
                }
                DropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Profile") },
                        onClick = {
                            expandedMenu = false
                            navController.navigate(ROUT_EDIT)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Log Out") },
                        onClick = {
                            expandedMenu = false
                            navController.navigate(ROUT_LOGIN)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Account", color = Color.Red) },
                        onClick = {
                            expandedMenu = false
                            showDeleteDialog = true
                        }
                    )
                }
            }

            // Profile Info Card: Half screen height and centered
            Card(
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = FintechLightBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = user!!.profilePictureUri,
                            contentDescription = "Profile Picture",
                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                            error = painterResource(id = R.drawable.ic_launcher_foreground),
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(user!!.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(user!!.email, fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(" ${user!!.phoneNumber}", fontSize = 12.sp, color = Color.LightGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoCard(icon = Icons.Default.Phone, title = "Phone", value = user!!.phoneNumber)
                InfoCard(icon = Icons.Default.Email, title = "Email", value = user!!.email)
                InfoCard(icon = painterResource(id = R.drawable.business), title = "Business", value = user!!.businessName)
                InfoCard(icon = painterResource(id = R.drawable.ic_launcher_foreground), title = "User ID", value = user!!.id.toString())
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Profile") },
                text = { Text("Are you sure? This action is permanent.") },
                confirmButton = {
                    TextButton(onClick = {
                        profileViewModel.deleteUserProfile(user!!.id)
                        showDeleteDialog = false
                        navController.navigate(ROUT_REGISTER) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
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
            CircularProgressIndicator(color = FintechTeal)
        }
    }
}


@Composable
fun InfoCard(icon: Any, title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FintechLightBackground),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon is Painter) {
                Icon(painter = icon, contentDescription = title, tint = FintechIconTint)
            } else if (icon is ImageVector) {
                Icon(imageVector = icon, contentDescription = title, tint = FintechIconTint)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontSize = 14.sp, color = Color.Gray)
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
