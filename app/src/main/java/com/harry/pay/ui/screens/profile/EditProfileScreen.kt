package com.harry.pay.ui.screens.profile

import androidx.compose.ui.tooling.preview.Preview

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.harry.pay.R
import com.harry.pay.model.User
import com.harry.pay.viewmodel.ProfileViewModel
import com.harry.pay.repository.UserRepository
import com.harry.pay.data.UserDao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    user: User,  // Receive current user details
    onSaveChanges: (User) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var businessName by remember { mutableStateOf(user.businessName) }
    var profilePictureUri by remember { mutableStateOf(user.profilePictureUri) }
    val context = LocalContext.current

    // Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            profilePictureUri = it.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Your Profile",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture Picker
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (profilePictureUri.isNotEmpty()) {
                AsyncImage(model = profilePictureUri, contentDescription = "Profile Picture")
            } else {
                IconButton(onClick = {
                    imagePickerLauncher.launch("image/*")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera_alt),
                        contentDescription = "Pick Profile Picture"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            leadingIcon = { Icon(painterResource(R.drawable.profile), contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(painterResource(R.drawable.email), contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone Number
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(painterResource(R.drawable.phone), contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Business Name
        OutlinedTextField(
            value = businessName,
            onValueChange = { businessName = it },
            label = { Text("Business Name") },
            leadingIcon = { Icon(painterResource(R.drawable.business), contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save Changes Button
        Button(
            onClick = {
                if (name.isBlank() || email.isBlank() || phoneNumber.isBlank() || businessName.isBlank()) {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedUser = User(
                        id = user.id, // Keep the same user ID
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = user.password, // Do not change password
                        businessName = businessName,
                        profilePictureUri = profilePictureUri
                    )
                    onSaveChanges(updatedUser)  // Save the changes
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()  // Navigate back after saving changes
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Save Changes", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    val navController = rememberNavController()

    val dummyUser = User(
        id = 1,
        name = "Jane Doe",
        email = "jane.doe@example.com",
        phoneNumber = "0712345678",
        password = "1234",
        businessName = "Doe Ventures",
        profilePictureUri = "" // Leave blank or use a valid local URI
    )

    EditProfileScreen(
        navController = navController,
        user = dummyUser,
        onSaveChanges = { updatedUser ->
            // Handle the save logic here (e.g., update user in the database or state)
            println("User profile updated: $updatedUser")
        }
    )
}
