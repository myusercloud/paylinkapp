package com.harry.pay.ui.screens.link

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.harry.pay.model.PaymentLink
import com.harry.pay.navigation.ROUT_SCAFFOLD
import com.harry.pay.ui.theme.FintechFieldFocused
import com.harry.pay.ui.theme.FintechIconTint
import com.harry.pay.ui.theme.FintechLightBackground
import com.harry.pay.ui.theme.FintechPrimary
import com.harry.pay.ui.theme.FintechTeal
import kotlinx.coroutines.delay
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
    var smartInput by remember { mutableStateOf("") }
    var isParsing by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(title, amount) {
        val slug = title.trim().replace("\\s+".toRegex(), "-")
        val cleanAmount = amount.trim()
        linkPreview = if (slug.isNotEmpty() && cleanAmount.isNotEmpty()) {
            "https://paylink.app/$slug/$cleanAmount"
        } else {
            ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(FintechLightBackground, Color.White)
                )
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = FintechIconTint
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI | Create Payment Link",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = FintechPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = FintechLightBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Smart Assistant", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "e.g. 'Request 2500 from Mike for groceries'",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = smartInput,
                        onValueChange = { smartInput = it },
                        label = { Text("Smart Input") },
                        placeholder = { Text("request 10000 from John for logo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FintechFieldFocused
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            isParsing = true
                            errorMessage = null
                            scope.launch {
                                delay(800)
                                val data = extractPaylinkData(smartInput)
                                if (data != null) {
                                    title = data.recipient
                                    description = data.reason
                                    amount = data.amount.toString()
                                } else {
                                    errorMessage = "❌ Couldn't parse input. Try: 'Request 3000 from James for branding'"
                                }
                                isParsing = false
                            }
                        },
                        enabled = !isParsing,
                        colors = ButtonDefaults.buttonColors(containerColor = FintechPrimary)
                    ) {
                        if (isParsing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Parsing...")
                        } else {
                            Text("Let AI Fill Form", color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Recipient Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FintechFieldFocused)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FintechFieldFocused)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d+(\\.\\d{0,2})?\$"))) {
                        amount = it
                    }
                },
                label = { Text("Amount (KES)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FintechFieldFocused)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (linkPreview.isNotEmpty()) {
                Text("🔗 Preview: $linkPreview", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Summary", fontWeight = FontWeight.Bold)
                        Text("To: $title", style = MaterialTheme.typography.bodySmall)
                        Text("For: $description", style = MaterialTheme.typography.bodySmall)
                        Text("Amount: KES $amount", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (title.isBlank() || description.isBlank() || amount.toDoubleOrNull() == null || amount.toDouble() <= 0) {
                        errorMessage = "⚠️ Please complete all fields correctly."
                        return@Button
                    }
                    val link = PaymentLink(
                        title = title,
                        description = description,
                        amount = amount.toDouble(),
                        link = linkPreview
                    )
                    onCreate(link)
                    scope.launch {
                        snackbarHostState.showSnackbar("✅ Link created successfully!")
                        delay(1000)
                        navController.navigate(ROUT_SCAFFOLD)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isParsing,
                colors = ButtonDefaults.buttonColors(containerColor = FintechTeal)
            ) {
                Text("🎉 Generate & Save Link", color = Color.White)
            }
        }

        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}




data class PaylinkData(
    val amount: Int,
    val recipient: String,
    val reason: String
)

fun extractPaylinkData(input: String): PaylinkData? {
    val regex = Regex("""request\s+(\d+)\s+from\s+([\w\s]+?)\s+for\s+(.+)""", RegexOption.IGNORE_CASE)
    val match = regex.find(input.trim()) ?: return null
    val (amountStr, recipient, reason) = match.destructured
    val amount = amountStr.toIntOrNull() ?: return null
    return PaylinkData(amount, recipient.trim(), reason.trim())
}


@Preview(showBackground = true)
@Composable
fun CreateLinkScreenPreview() {
    CreateLinkScreen(
        navController = rememberNavController(),
        onCreate = {}
    )
}
