package com.harry.pay.ui.screens.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.harry.pay.R
import com.harry.pay.model.PaymentLink
import com.harry.pay.model.User
import com.harry.pay.navigation.ROUT_CREATE_LINK
import com.harry.pay.navigation.ROUT_PROFILE
import com.harry.pay.navigation.ROUT_SCAFFOLD
import com.harry.pay.ui.screens.scaffold.FilterChip
import com.harry.pay.ui.screens.scaffold.LinkItem
import com.harry.pay.viewmodel.PaymentLinkViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val FintechTeal = Color(0xFF039D86)
val FintechDarkTeal = Color(0xFF00695C)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    currentUser: User,
    paymentLinks: List<PaymentLink>,
    linkViewModel: PaymentLinkViewModel
) {
    val context = LocalContext.current
    var linkToDelete by remember { mutableStateOf<PaymentLink?>(null) }

    val totalLinks = paymentLinks.size
    val totalValue = paymentLinks.sumOf { it.amount }

    var selectedFilter by remember { mutableStateOf("All") }
    var showMenu by remember { mutableStateOf(false) }

    val filteredLinks = when (selectedFilter) {
        "Today" -> paymentLinks.filter {
            val createdDate = Instant.ofEpochMilli(it.createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            createdDate == LocalDate.now()
        }
        "Amount > 1000" -> paymentLinks.filter { it.amount > 1000 }
        else -> paymentLinks
    }


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        // Top Header with Settings
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentUser.profilePictureUri.isNotEmpty()) {
                    SubcomposeAsyncImage(
                        model = currentUser.profilePictureUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray)
                            )
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "Default Profile Picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Hi, ${currentUser.name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FintechTeal
                    )
                    Text(
                        text = "Here's your latest overview",
                        color = FintechDarkTeal
                    )
                }
            }

            IconButton(onClick = { showMenu = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.settings), // Add your settings icon in drawable
                    contentDescription = "Settings",
                    tint = FintechTeal
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Profile") },
                    onClick = {
                        showMenu = false
                        navController.navigate(ROUT_PROFILE)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        showMenu = false
                        // Handle logout logic here
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Links", fontWeight = FontWeight.Bold)
                    Text("$totalLinks", fontSize = 18.sp, color = FintechDarkTeal)
                }
                Column {
                    Text("Total Value", fontWeight = FontWeight.Bold)
                    Text("Ksh. $totalValue", fontSize = 18.sp, color = FintechDarkTeal)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Button(
            onClick = { navController.navigate(ROUT_CREATE_LINK) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = FintechTeal)
        ) {
            Text("Create Payment Link", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(ROUT_SCAFFOLD) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = FintechTeal)
        ) {
            Text("Go to Communities", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(ROUT_PROFILE) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = FintechTeal)
        ) {
            Text("View Profile", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Filters
        Text("Filter Links", fontWeight = FontWeight.SemiBold, color = FintechTeal)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            FilterChip("All", selected = selectedFilter == "All") { selectedFilter = "All" }
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip("Today", selected = selectedFilter == "Today") { selectedFilter = "Today" }
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip("Amount > 1000", selected = selectedFilter == "Amount > 1000") { selectedFilter = "Amount > 1000" }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredLinks.isEmpty()) {
            Text(
                "No links found for the selected filter.",
                color = Color.Gray,
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                modifier = Modifier.padding(top = 20.dp)
            )
        } else {
            filteredLinks.forEach { link ->
                val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color(0xFFF5F5F5))
                        .padding(12.dp)
                ) {
                    LinkItem(
                        name = "Recipient: ${link.title}",
                        description = link.description,
                        amount = "Ksh. ${link.amount}",
                        date = formattedDate,
                        onEdit = { navController.navigate("edit_link/${link.id}") },
                        onDelete = { linkToDelete = link },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Copied Link", link.link)
                            clipboard.setPrimaryClip(clip)
                        },
                        onShare = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, link.link)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    if (linkToDelete != null) {
        AlertDialog(
            onDismissRequest = { linkToDelete = null },
            title = { Text("Delete Link") },
            text = {
                Text("Are you sure you want to delete this payment link? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        linkViewModel.deleteLink(linkToDelete!!)
                        linkToDelete = null
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { linkToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
