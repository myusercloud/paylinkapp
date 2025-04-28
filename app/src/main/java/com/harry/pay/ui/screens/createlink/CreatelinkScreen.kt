package com.harry.pay.ui.screens.createlink

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CreateLinkScreen(navController: NavController) {
    // States to hold the form data
    var linkName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var linkDescription by remember { mutableStateOf("") }
    var linkPreview by remember { mutableStateOf<String?>(null) }

    // Function to generate the preview link
    fun generatePreviewLink(name: String, amount: String, description: String): String {
        return "https://paylink.com/$name?amount=$amount&description=$description"
    }

    // Create Link Screen UI
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Create a Payment Link",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Link Name Input
        OutlinedTextField(
            value = linkName,
            onValueChange = { linkName = it },
            label = { Text("Link Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { /* Do something when done, e.g., focus change */ }
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Link Description Input
        OutlinedTextField(
            value = linkDescription,
            onValueChange = { linkDescription = it },
            label = { Text("Link Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Button to generate link preview
        Button(
            onClick = {
                if (linkName.isNotEmpty() && amount.isNotEmpty() && linkDescription.isNotEmpty()) {
                    linkPreview = generatePreviewLink(linkName, amount, linkDescription)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Generate Link Preview")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Show the link preview if available
        linkPreview?.let {
            LinkPreview(it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation buttons for further steps (like saving or going back)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    // Add logic to save the link or navigate back
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save Link")
            }

            Button(
                onClick = {
                    // Navigate back to the home screen or previous screen
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
fun LinkPreview(previewLink: String) {
    Text(
        text = "Link Preview: $previewLink",
        fontSize = 16.sp,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun CreateLinkScreenPreview() {
    CreateLinkScreen(navController = rememberNavController())
}
