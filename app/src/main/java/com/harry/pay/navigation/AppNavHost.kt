package com.harry.pay.navigation

import android.window.SplashScreen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.harry.pay.data.UserDatabase
import com.harry.pay.repository.UserRepository
import com.harry.pay.repository.PaymentLinkRepository
import com.harry.pay.ui.screens.RegisterScreen
import com.harry.pay.ui.screens.about.AboutScreen
import com.harry.pay.ui.screens.auth.LoginScreen
import com.harry.pay.ui.screens.profile.EditProfileScreen
import com.harry.pay.ui.screens.scaffold.ScaffoldScreen
import com.harry.pay.ui.screens.home.HomeScreen
import com.harry.pay.ui.screens.link.CreateLinkScreen
import com.harry.pay.ui.screens.link.EditLinkScreen
import com.harry.pay.viewmodel.AuthViewModel
import com.harry.pay.viewmodel.AuthViewModelFactory
import com.harry.pay.model.PaymentLink
import com.harry.pay.ui.screens.splash.PayLinkSplashScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {
    val context = LocalContext.current
    val appDatabase = UserDatabase.getDatabase(context)
    val authRepository = UserRepository(appDatabase.userDao())
    val authViewModelFactory = AuthViewModelFactory(authRepository)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Register
        composable(ROUT_REGISTER) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        // Login
        composable(ROUT_LOGIN) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            LoginScreen(
                onLogin = { username, password -> authViewModel.loginUser(username, password) },
                navController = navController,
                onSuccess = {
                    navController.navigate(ROUT_HOME) {
                        popUpTo(ROUT_LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Home
        composable(ROUT_HOME) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            val userState by authViewModel.user.collectAsState()
            val isLoading by authViewModel.isLoading.collectAsState()
            val errorState by authViewModel.error.collectAsState()

            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorState != null) {
                Text("Error: $errorState")
            } else {
                userState?.let { currentUser ->
                    HomeScreen(navController = navController, currentUser = currentUser)
                }
            }
        }

        // About
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }
        composable(ROUT_SPLASH) {
            PayLinkSplashScreen(navController)
        }

        // Profile (routes to AboutScreen for now)
        composable(ROUT_PROFILE) {
            AboutScreen(navController)
        }

        // Edit Profile
        composable(ROUT_EDIT) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            val userState by authViewModel.user.collectAsState()

            userState?.let { currentUser ->
                EditProfileScreen(
                    navController = navController,
                    user = currentUser,
                    onSaveChanges = { updatedUser ->
                        authViewModel.updateUser(updatedUser)
                        navController.popBackStack()
                    }
                )
            }
        }

        // Scaffold wrapper
        composable(ROUT_SCAFFOLD) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            val userState by authViewModel.user.collectAsState()
            userState?.let { currentUser ->
                ScaffoldScreen(navController = navController, currentUser = currentUser)
            }
        }

        // Create Payment Link
        composable(ROUT_CREATE_LINK) {
            val repo = PaymentLinkRepository(appDatabase.paymentLinkDao())
            val coroutineScope = rememberCoroutineScope()

            CreateLinkScreen(
                navController = navController,
                onCreate = { newLink ->
                    coroutineScope.launch {
                        repo.insert(newLink)
                        navController.popBackStack()
                    }
                }
            )
        }


        // Edit Payment Link
        composable("edit_link/{linkId}") { backStackEntry ->
            val linkId = backStackEntry.arguments?.getString("linkId")?.toIntOrNull()
            val repo = PaymentLinkRepository(appDatabase.paymentLinkDao())

            var link by remember { mutableStateOf<PaymentLink?>(null) }
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(linkId) {
                link = repo.getAll().find { it.id == linkId }
            }

            link?.let {
                EditLinkScreen(
                    navController = navController,
                    paymentLink = it,
                    onUpdate = { updated ->
                        coroutineScope.launch {
                            repo.update(updated)
                            navController.popBackStack()
                        }
                    },
                    onDelete = { toDelete ->
                        coroutineScope.launch {
                            repo.delete(toDelete)
                            navController.popBackStack()
                        }
                    }
                )
            } ?: Text("Loading payment link...")
        }
    }
}
