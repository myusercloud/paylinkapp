import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.harry.pay.navigation.ROUT_LOGIN
import com.harry.pay.navigation.ROUT_REGISTER
import kotlinx.coroutines.delay

@Composable
fun PayLinkSplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(7000)
        navController.navigate(ROUT_LOGIN)
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF06DCBE), Color(0xFF013430))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Lottie Animation
            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("car_loading.json"))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(450.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PayLink",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "AI Link. Send. Get Paid.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
            Text(
                text = "Powered by Open AI",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}
