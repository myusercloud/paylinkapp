package com.harry.pay.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.harry.pay.model.User
import com.harry.pay.ui.screens.scaffold.FilterChip
import com.harry.pay.ui.screens.scaffold.LinkItem

@Composable
fun HomeScreen(navController: NavController, currentUser: User) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        // Greeting with profile picture
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (currentUser.profilePictureUri.isNotEmpty()) {
                AsyncImage(
                    model = currentUser.profilePictureUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            } else {
                // Fallback circle
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Hi there, ${currentUser.name}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = "Ready to manage your links today?")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Navigate to Create Link */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Link")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* Navigate to Communities */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Your Communities")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* Navigate to Payment History */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Payment History")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* Navigate to User Profile */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "User Profile")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Your Links", fontSize = 20.sp, fontWeight = FontWeight.Bold)

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

        // Dummy list of links
        LinkItem("Recipient 1", "Description of Link 1", "£10", "Date created")
        Spacer(modifier = Modifier.height(8.dp))
        LinkItem("Recipient 2", "Description of Link 2", "£20", "Date created")
        Spacer(modifier = Modifier.height(8.dp))
        LinkItem("Recipient 3", "Description of Link 3", "£30", "Date created")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val dummyUser = User(
        id = 1,
        name = "Harry",
        email = "harry@example.com",
        phoneNumber = "0712345678",
        password = "1234",
        businessName = "PayLink Ventures",
        profilePictureUri = "" // Replace with a real URI to see the image in preview
    )

    HomeScreen(navController = rememberNavController(), currentUser = dummyUser)
}
