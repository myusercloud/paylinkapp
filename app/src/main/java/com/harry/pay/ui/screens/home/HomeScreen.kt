package com.harry.pay.ui.screens.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import com.harry.pay.ui.screens.scaffold.FilterChip
import com.harry.pay.ui.screens.scaffold.LinkItem
import com.harry.pay.viewmodel.PaymentLinkViewModel

val FintechTeal = Color(0xFF039D86)
val FintechDarkTeal = Color(0xFF00695C)

@Composable
fun HomeScreen(
    navController: NavController,
    currentUser: User,
    paymentLinks: List<PaymentLink>,
    linkViewModel: PaymentLinkViewModel
) {
    val context = LocalContext.current
    var linkToDelete by remember { mutableStateOf<PaymentLink?>(null) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
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
                    text = "Hi there, ${currentUser.name}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FintechTeal
                )
                Text(
                    text = "Ready to manage your links today?",
                    color = FintechDarkTeal
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate(ROUT_CREATE_LINK) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = FintechTeal)
        ) {
            Text("Create Link", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(ROUT_CREATE_LINK) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = FintechTeal)
        ) {
            Text("Your Communities", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(ROUT_PROFILE) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = FintechTeal)
        ) {
            Text("User Profile", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your Links",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = FintechTeal
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip("All")
            FilterChip("Pending")
            FilterChip("Completed")
        }

        Spacer(modifier = Modifier.height(16.dp))

        paymentLinks.forEach { link ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color(0xFFF5F5F5))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        LinkItem(
                            name = "Recipient name: ${link.title}",
                            description = link.description,
                            amount = "Ksh. ${link.amount}",
                            date = "2025-04-30", // Replace with actual date if needed
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
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    // Confirmation Dialog
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
