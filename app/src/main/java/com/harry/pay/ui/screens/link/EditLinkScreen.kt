package com.harry.pay.ui.screens.link

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.harry.pay.model.PaymentLink
import kotlinx.coroutines.launch

@Composable
fun EditLinkScreen(
    navController: NavController,
    paymentLink: PaymentLink,
    onUpdate: (PaymentLink) -> Unit,
    onDelete: (PaymentLink) -> Unit
) {
    var title by remember { mutableStateOf(paymentLink.title) }
    var description by remember { mutableStateOf(paymentLink.description) }
    var amount by remember { mutableStateOf(paymentLink.amount.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Validate amount (should be a positive number)
    val isAmountValid = amount.toDoubleOrNull()?.takeIf { it > 0 } != null
    val isFormValid = title.isNotEmpty() && description.isNotEmpty() && isAmountValid

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Edit Payment Link", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Title Input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = title.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            isError = description.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = {
                // Allow only valid numeric input
                if (it.isEmpty() || it.matches(Regex("^[0-9]+(\\.[0-9]{0,2})?\$"))) {
                    amount = it
                }
            },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = !isAmountValid
        )
        if (!isAmountValid) {
            Text("Amount must be a valid positive number", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preview the updated link
        Text("Preview Link: ${paymentLink.link}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Update Button
        Button(
            onClick = {
                if (isFormValid) {
                    scope.launch {
                        val updatedLink = paymentLink.copy(
                            title = title,
                            description = description,
                            amount = amount.toDouble()
                        )
                        onUpdate(updatedLink)
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

        // Delete Button
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    onDelete(paymentLink)
                    navController.popBackStack()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Link")
        }

        // Show error message if any
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditLinkScreenPreview() {
    val dummyLink = PaymentLink(
        id = 1,
        title = "Test Payment",
        description = "For testing",
        amount = 25.0,
        link = "https://paylink.app/xyz123"
    )
    EditLinkScreen(
        navController = rememberNavController(),
        paymentLink = dummyLink,
        onUpdate = {},
        onDelete = {}
    )
}
