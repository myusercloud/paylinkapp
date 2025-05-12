package com.harry.pay.navigation

import PayLinkSplashScreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.harry.pay.data.UserDatabase
import com.harry.pay.repository.PaymentLinkRepository
import com.harry.pay.repository.UserRepository
import com.harry.pay.ui.screens.*
import com.harry.pay.ui.screens.about.AboutScreen
import com.harry.pay.ui.screens.auth.LoginScreen
import com.harry.pay.ui.screens.home.HomeScreen
import com.harry.pay.ui.screens.link.CreateLinkScreen
import com.harry.pay.ui.screens.link.EditLinkScreen
import com.harry.pay.ui.screens.profile.EditProfileScreen
import com.harry.pay.ui.screens.scaffold.ScaffoldScreen
import com.harry.pay.viewmodel.*
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
    val linkRepository = PaymentLinkRepository(appDatabase.paymentLinkDao())

    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, context))
    val linkViewModel: PaymentLinkViewModel = viewModel(factory = PaymentLinkViewModelFactory(linkRepository))

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_SPLASH) {
            PayLinkSplashScreen(navController)
        }

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
            val paymentLinks by linkViewModel.links.collectAsState()

            if (userState != null) {
                ScaffoldScreen(navController = navController, currentUser = userState!!, paymentLinks = paymentLinks)
            } else {
                CircularProgressIndicator()
            }
        }

        composable(ROUT_HOME) {
            val userState by authViewModel.user.collectAsState()
            val paymentLinksState by linkViewModel.links.collectAsState() // Collect links from ViewModel

            if (userState != null) {
                HomeScreen(
                    navController = navController,
                    currentUser = userState!!,
                    paymentLinks = paymentLinksState,
                    linkViewModel = linkViewModel
                )
            } else {
                Text("Loading user...")
            }
        }


        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        composable(ROUT_PROFILE) {
            AboutScreen(navController) // Replace with profile if needed
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
            CreateLinkScreen(
                navController = navController,
                onCreate = { newLink ->
                    linkViewModel.addLink(newLink) // Add link to the ViewModel
                    navController.popBackStack() // Navigate back
                }
            )
        }


        composable("edit_link/{linkId}") { backStackEntry ->
            val linkId = backStackEntry.arguments?.getString("linkId")?.toIntOrNull()
            val links by linkViewModel.links.collectAsState()
            val link = links.find { it.id == linkId }

            link?.let {
                EditLinkScreen(
                    navController = navController,
                    paymentLink = it,
                    onUpdate = { updated ->
                        linkViewModel.updateLink(updated)
                        navController.popBackStack()
                    },
                    onDelete = { toDelete ->
                        linkViewModel.deleteLink(toDelete)
                        navController.popBackStack()
                    }
                )
            } ?: Text("Loading payment link...")
        }
    }
}
