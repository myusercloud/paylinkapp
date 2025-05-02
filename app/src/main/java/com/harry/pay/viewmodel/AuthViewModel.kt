package com.harry.pay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pay.model.User
import com.harry.pay.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> =  _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Registers a new user
     */
    fun registerUser(user: User) = safeLaunch {
        repository.registerUser(user)
    }

    /**
     * Attempts login and updates current user state
     */
    fun loginUser(name: String, password: String) = safeLaunch {
        _error.value = null
        _loginSuccess.value = false

        val user = repository.loginUser(name, password)
        if (user != null) {
            _user.value = user
            _loginSuccess.value = true
        } else {
            _error.value = "Invalid username or password"
        }
    }


    /**
     * Updates current user and state
     */
    fun updateUser(user: User) = safeLaunch {
        repository.updateUser(user)
        _user.value = user
    }

    /**
     * Deletes the current user and clears state
     */
    fun deleteUser(user: User) = safeLaunch {
        repository.deleteUser(user)
        _user.value = null
    }

    /**
     * Fetches a user by ID and updates state
     */
    fun fetchUserById(id: Int) = safeLaunch {
        _user.value = repository.getUserById(id)
    }

    /**
     * Clears current error state
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Launches a coroutine with loading and error handling
     */
    private fun safeLaunch(block: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                block()
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
