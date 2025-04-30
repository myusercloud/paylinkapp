package com.harry.pay.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.harry.pay.R
import com.harry.pay.model.User
import com.harry.pay.viewmodel.AuthViewModel
import com.harry.pay.repository.UserRepository
import com.harry.pay.data.UserDao
import com.harry.pay.navigation.ROUT_LOGIN
import com.harry.pay.navigation.ROUT_REGISTER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var profilePictureUri by remember { mutableStateOf("") }
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
        AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = "Create Your Account",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture Picker
        Box(
            modifier = Modifier
                .size(100.dp)
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

        Spacer(modifier = Modifier.height(8.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password (4-digit)") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(painterResource(R.drawable.lock), contentDescription = null) },
            trailingIcon = {
                val icon = if (passwordVisible) R.drawable.visibility else R.drawable.visibilityoff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painterResource(icon), contentDescription = "Toggle password visibility")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(painterResource(R.drawable.lock), contentDescription = null) },
            trailingIcon = {
                val icon = if (confirmPasswordVisible) R.drawable.visibility else R.drawable.visibilityoff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(painterResource(icon), contentDescription = "Toggle confirm password visibility")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {
                if (name.isBlank() || email.isBlank() || phoneNumber.isBlank() ||
                    password.isBlank() || confirmPassword.isBlank() ||
                    businessName.isBlank() || profilePictureUri.isBlank()
                ) {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else if (password.length != 4) {
                    Toast.makeText(context, "Password must be 4 digits", Toast.LENGTH_SHORT).show()
                } else {
                    val user = User(
                        id = 0, // Let Room auto-generate this
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        businessName = businessName,
                        profilePictureUri = profilePictureUri
                    )
                    authViewModel.registerUser(user)
                    Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
                    onRegisterSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Register", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {
            navController.navigate(ROUT_LOGIN) {
                popUpTo(ROUT_REGISTER) { inclusive = true }
            }
        }) {
            Text("Already have an account? Login")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    val dummyDao = object : UserDao {
        override suspend fun registerUser(user: User) {}
        override suspend fun loginUser(name: String, password: String): User? = null
        override suspend fun updateUser(user: User) {}
        override suspend fun deleteUser(user: User) {}
        override suspend fun getUserById(id: Int): User? = null
    }
    val dummyRepo = UserRepository(dummyDao)
    RegisterScreen(authViewModel = AuthViewModel(dummyRepo), navController = navController) {}
}

