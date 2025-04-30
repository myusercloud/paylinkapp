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
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Edit Payment Link", style = MaterialTheme.typography.headlineSmall)
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
                    val updatedLink = paymentLink.copy(
                        title = title,
                        description = description,
                        amount = amount.toDoubleOrNull() ?: 0.0
                    )
                    onUpdate(updatedLink)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Link")
        }

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
