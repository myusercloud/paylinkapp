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
import com.harry.pay.navigation.ROUT_SCAFFOLD
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun CreateLinkScreen(
    navController: NavController,
    onCreate: (PaymentLink) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var linkPreview by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Link Preview is updated whenever title, description, or amount changes
    LaunchedEffect(title, amount) {
        val sanitizedTitle = title.trim().replace("\\s+".toRegex(), "-")
        val sanitizedAmount = amount.trim()
        linkPreview = if (sanitizedTitle.isNotEmpty() && sanitizedAmount.isNotEmpty()) {
            "https://paylink.app/$sanitizedTitle/$sanitizedAmount"
        } else {
            ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Create Payment Link", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Title Input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = {
                // Allow only numeric input
                if (it.isEmpty() || it.matches(Regex("^[0-9]+(\\.[0-9]{0,2})?\$"))) {
                    amount = it
                }
            },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Link Preview
        Text("Preview Link: $linkPreview", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Create Link Button
        Button(
            onClick = {
                // Validation logic
                if (title.isEmpty() || description.isEmpty() || amount.toDoubleOrNull() == null || amount.toDouble() <= 0) {
                    errorMessage = "Please fill out all fields correctly."
                } else {
                    errorMessage = null
                    val link = PaymentLink(
                        title = title,
                        description = description,
                        amount = amount.toDouble(),
                        link = linkPreview
                    )
                    onCreate(link)
                    navController.navigate(ROUT_SCAFFOLD)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotEmpty() && description.isNotEmpty() && amount.toDoubleOrNull() != null && amount.toDouble() > 0
        ) {
            Text("Generate & Save Link")
        }

        // Error message if validation fails
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateLinkScreenPreview() {
    CreateLinkScreen(
        navController = rememberNavController(),
        onCreate = {}
    )
}
