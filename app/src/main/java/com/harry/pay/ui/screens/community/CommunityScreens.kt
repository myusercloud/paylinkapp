package com.harry.pay.ui.screens.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.harry.pay.R

data class CommunityCircle(
    val name: String,
    val description: String,
    val iconRes: Int,
    val tags: List<String> = listOf("Open", "Popular", "Active") // concept tags
)

@Composable
fun CommunityCirclesScreen(navController: NavController) {
    val communityCircles = listOf(
        CommunityCircle("Tech Innovators", "A community for tech enthusiasts to share ideas and innovations.", R.drawable.pple),
        CommunityCircle("Designers Hub", "A place for designers to collaborate and learn from each other.", R.drawable.pple),
        CommunityCircle("Food Lovers", "Explore new recipes and culinary experiences.", R.drawable.pple),
        CommunityCircle("Fitness Freaks", "Workouts, motivation, and health tips.", R.drawable.pple)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                )
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Community Circles",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Text(
                "Discover and join exclusive communities tailored for creators, builders, and dreamers.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(communityCircles.size) { index ->
                    CommunityCircleCard(communityCircle = communityCircles[index])
                }
            }
        }
    }
}

@Composable
fun CommunityCircleCard(communityCircle: CommunityCircle) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* future navigation */ }
            .padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = communityCircle.iconRes),
                contentDescription = "Circle Icon",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    communityCircle.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    communityCircle.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    communityCircle.tags.forEach { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text(tag, fontSize = 12.sp) },
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = Color.White.copy(alpha = 0.9f),
                                containerColor = Color.White.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommunityScreen() {
    CommunityCirclesScreen(navController = rememberNavController())
}
