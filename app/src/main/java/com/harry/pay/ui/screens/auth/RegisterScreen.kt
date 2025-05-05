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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.harry.pay.R
import com.harry.pay.data.UserDao
import com.harry.pay.model.User
import com.harry.pay.navigation.ROUT_LOGIN
import com.harry.pay.navigation.ROUT_REGISTER
import com.harry.pay.repository.UserRepository
import com.harry.pay.ui.theme.*
import com.harry.pay.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var profilePictureUri by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { profilePictureUri = it.toString() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 8.dp)
        )

        AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = "Create Account",
                fontSize = 24.sp,
                color = FintechPrimary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // Profile Image Picker
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(FintechLightBackground),
            contentAlignment = Alignment.Center
        ) {
            if (profilePictureUri.isNotEmpty()) {
                AsyncImage(model = profilePictureUri, contentDescription = "Profile")
            } else {
                IconButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera_alt),
                        contentDescription = "Pick Profile Picture",
                        tint = FintechIconTint
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Text Fields for user details
        CustomTextField(value = name, onValueChange = { name = it }, label = "Name", icon = R.drawable.profile)
        CustomTextField(value = email, onValueChange = { email = it }, label = "Email", icon = R.drawable.email, keyboardType = KeyboardType.Email)
        CustomTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = "Phone Number", icon = R.drawable.phone, keyboardType = KeyboardType.Phone)
        CustomTextField(value = businessName, onValueChange = { businessName = it }, label = "Business Name", icon = R.drawable.business)

        // Password and Confirm Password Fields
        PasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Password (4-digit)",
            visible = passwordVisible,
            onToggleVisibility = { passwordVisible = !passwordVisible }
        )

        PasswordField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            visible = confirmPasswordVisible,
            onToggleVisibility = { confirmPasswordVisible = !confirmPasswordVisible }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    listOf(name, email, phoneNumber, password, confirmPassword, businessName, profilePictureUri).any { it.isBlank() } ->
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                    password != confirmPassword ->
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    password.length != 4 ->
                        Toast.makeText(context, "Password must be 4 digits", Toast.LENGTH_SHORT).show()
                    else -> {
                        val user = User(
                            id = 0,
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
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FintechPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Register", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {
            navController.navigate(ROUT_LOGIN) {
                popUpTo(ROUT_REGISTER) { inclusive = true }
            }
        }) {
            Text("Already have an account? Login", color = FintechPrimary)
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Int,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(painterResource(icon), contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = FintechFieldFocused,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = FintechPrimary,
            unfocusedLabelColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = { Icon(painterResource(R.drawable.lock), contentDescription = null) },
        trailingIcon = {
            val icon = if (visible) R.drawable.visibility else R.drawable.visibilityoff
            IconButton(onClick = onToggleVisibility) {
                Icon(painterResource(icon), contentDescription = null)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = FintechFieldFocused,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = FintechPrimary,
            unfocusedLabelColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}
/*
@Preview(showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    val dummyDao = object : UserDao {
        override suspend fun registerUser(user: User) {}
        override suspend fun loginUser(name: String, password: String): User? = null
        override suspend fun updateUser(user: User) {}
        override suspend fun deleteUser(user: User) {}
        override suspend fun deleteUserById(userId: Int) {}
        override suspend fun getUserById(id: Int): User? = null
    }
    val dummyRepo = UserRepository(dummyDao)
    RegisterScreen(authViewModel = AuthViewModel(dummyRepo), navController = navController) {}
}
*/