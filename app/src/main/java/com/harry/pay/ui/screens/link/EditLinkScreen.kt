package com.harry.pay.ui.screens.link

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.harry.pay.model.PaymentLink
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLinkScreen(
    navController: NavController,
    paymentLink: PaymentLink,
    onUpdate: (PaymentLink) -> Unit,
    onDelete: (PaymentLink) -> Unit
) {
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf(paymentLink.title) }
    var description by remember { mutableStateOf(paymentLink.description) }
    var amount by remember { mutableStateOf(paymentLink.amount.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val isAmountValid = amount.toDoubleOrNull()?.takeIf { it > 0 } != null
    val isFormValid = title.isNotEmpty() && description.isNotEmpty() && isAmountValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Payment Link") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0A0F1F), Color(0xFF1A2942))
                    )
                )
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title Input
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = title.isEmpty()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description Input
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = description.isEmpty()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Amount Input
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {
                            if (it.isEmpty() || it.matches(Regex("^[0-9]+(\\.[0-9]{0,2})?\$"))) {
                                amount = it
                            }
                        },
                        label = { Text("Amount") },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isAmountValid
                    )
                    if (!isAmountValid) {
                        Text(
                            "Amount must be a valid positive number",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Link Preview
                    Text(
                        "🔗 Preview:",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        paymentLink.link,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Update Button
                    Button(
                        onClick = {
                            if (isFormValid) {
                                scope.launch {
                                    onUpdate(
                                        paymentLink.copy(
                                            title = title,
                                            description = description,
                                            amount = amount.toDouble()
                                        )
                                    )
                                    snackbarHostState.showSnackbar("Payment link updated successfully.")
                                    navController.popBackStack()
                                }
                            } else {
                                errorMessage = "Please fill out all fields correctly."
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFormValid
                    ) {
                        Text("Update Link")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Delete Button
                    Button(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete Link")
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Editing a payment link lets you update recipient, reason, and amount while preserving the original link for tracking.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Link?") },
                text = { Text("This action cannot be undone. Are you sure you want to delete this link?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        scope.launch {
                            onDelete(paymentLink)
                            snackbarHostState.showSnackbar("Link deleted.")
                            navController.popBackStack()
                        }
                    }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditLinkScreenPreview() {
    val dummyLink = PaymentLink(
        id = 1,
        title = "Demo",
        description = "Consulting Payment",
        amount = 299.99,
        link = "https://paylink.app/harry/299"
    )
    EditLinkScreen(
        navController = rememberNavController(),
        paymentLink = dummyLink,
        onUpdate = {},
        onDelete = {}
    )
}
