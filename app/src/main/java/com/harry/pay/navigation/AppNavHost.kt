package com.harry.pay.navigation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.harry.pay.ui.screens.splash.PayLinkSplashScreen
import com.harry.pay.viewmodel.AuthViewModel
import com.harry.pay.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.harry.pay.model.PaymentLink

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {
    val context = LocalContext.current
    val appDatabase = UserDatabase.getDatabase(context)
    val authRepository = UserRepository(appDatabase.userDao())
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_REGISTER) {
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        composable(ROUT_LOGIN) {
            val loginSuccess by authViewModel.loginSuccess.collectAsState()
            val loginError by authViewModel.error.collectAsState()
            val isLoading by authViewModel.isLoading.collectAsState()

            LoginScreen(
                onLogin = { username, password -> authViewModel.loginUser(username, password) },
                navController = navController,
                onSuccess = {
                    navController.navigate(ROUT_SCAFFOLD) {
                        popUpTo(ROUT_LOGIN) { inclusive = true }
                    }
                },
                loginSuccess = loginSuccess,
                error = loginError,
                isLoading = isLoading
            )
        }

        composable(ROUT_SCAFFOLD) {
            val userState by authViewModel.user.collectAsState()
            if (userState != null) {
                ScaffoldScreen(navController = navController, currentUser = userState!!)
            } else {
                CircularProgressIndicator()
            }
        }

        composable(ROUT_HOME) {
            val userState by authViewModel.user.collectAsState()
            if (userState != null) {
                HomeScreen(navController = navController, currentUser = userState!!)
            } else {
                Text("Loading user...")
            }
        }

        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        composable(ROUT_SPLASH) {
            PayLinkSplashScreen(navController)
        }

        composable(ROUT_PROFILE) {
            AboutScreen(navController)
        }

        composable(ROUT_EDIT) {
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
