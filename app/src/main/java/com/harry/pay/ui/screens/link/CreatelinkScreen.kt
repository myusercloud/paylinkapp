package com.harry.pay.ui.screens.link

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.harry.pay.model.PaymentLink
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
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Create Payment Link", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    val link = PaymentLink(
                        title = title,
                        description = description,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        link = "https://paylink.app/${UUID.randomUUID()}"
                    )
                    onCreate(link)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate & Save Link")
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
