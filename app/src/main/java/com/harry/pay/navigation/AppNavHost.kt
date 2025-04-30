package com.harry.pay.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.harry.pay.data.UserDatabase
import com.harry.pay.repository.UserRepository
import com.harry.pay.ui.screens.RegisterScreen
import com.harry.pay.ui.screens.about.AboutScreen
import com.harry.pay.ui.screens.auth.LoginScreen
import com.harry.pay.ui.screens.profile.EditProfileScreen
import com.harry.pay.ui.screens.scaffold.ScaffoldScreen
import com.harry.pay.viewmodel.AuthViewModel
import com.harry.pay.viewmodel.AuthViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.collectAsState
import com.harry.pay.ui.screens.home.HomeScreen
import com.harry.pay.model.User



@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_REGISTER
) {
    // ✅ Get context for Room database
    val context = LocalContext.current

    // ✅ Create database, repository, and ViewModelFactory
    val appDatabase = UserDatabase.getDatabase(context)
    val authRepository = UserRepository(appDatabase.userDao())
    val authViewModelFactory = AuthViewModelFactory(authRepository)


    // ✅ NavHost setup
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_SCAFFOLD) {
            ScaffoldScreen(navController)
        }
        composable(ROUT_HOME) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            val userState by authViewModel.user.collectAsState()

            userState?.let { currentUser ->
                HomeScreen(navController = navController, currentUser = currentUser)
            }
        }
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }
        composable(ROUT_PROFILE) {
            AboutScreen(navController)
        }
        composable(ROUT_EDIT) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            val userState by authViewModel.user.collectAsState()

            userState?.let { currentUser ->
                EditProfileScreen(
                    navController = navController,
                    user = currentUser,
                    onSaveChanges = { updatedUser ->
                        authViewModel.updateUser(updatedUser)
                        navController.popBackStack() // Go back after saving
                    }
                )
            }
        }

        // ✅ Register Screen
        composable(ROUT_REGISTER) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        // ✅ Login Screen
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
    }
}
