package com.harry.pay.ui.screens.community

import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.harry.pay.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

@Composable
fun CommunityCirclesScreen(navController: NavController) {
    // Dummy data for community circles
    val communityCircles = listOf(
        CommunityCircle("Tech Innovators", "A community for tech enthusiasts to share ideas and innovations.", R.drawable.pple),
        CommunityCircle("Designers Hub", "A place for designers to collaborate and learn from each other.", R.drawable.pple),
        CommunityCircle("Food Lovers", "Join this community to explore new recipes and culinary experiences.", R.drawable.pple),
        CommunityCircle("Fitness Freaks", "A fitness community for workout routines, motivation, and health tips.", R.drawable.pple)
    )

    // UI layout for the community circles
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Community Circles",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // LazyColumn to display the list of community circles
        LazyColumn {
            items(communityCircles.size) { index ->
                val circle = communityCircles[index]
                CommunityCircleCard(communityCircle = circle, navController = navController)
            }
        }
    }
}

@Composable
fun CommunityCircleCard(communityCircle: CommunityCircle, navController: NavController) {
    // Card representing each community circle
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Navigate to the community details screen (you can pass more info if needed)
                navController.navigate("community_details_screen/${communityCircle.name}")
            },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle image for community icon
            Image(
                painter = painterResource(id = communityCircle.iconRes),
                contentDescription = "Community Icon",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Community circle name and description
            Column {
                Text(
                    text = communityCircle.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = communityCircle.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


data class CommunityCircle(val name: String, val description: String, val iconRes: Int)

@Preview(showBackground = true)
@Composable
fun CommunityCirclesScreenPreview() {
    CommunityCirclesScreen(navController = rememberNavController())
}
