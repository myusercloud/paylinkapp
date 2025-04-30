package com.harry.pay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pay.model.User
import com.harry.pay.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // User state with loading and error states
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false) // Track loading state
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null) // Track errors
    val error: StateFlow<String?> = _error

    init {
        // Fetch user immediately if needed, for example, if you need to persist login session
        // fetchUserById(id) or you could use any session management.
    }

    fun registerUser(user: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.registerUser(user)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message // Set error message
            }
        }
    }

    fun loginUser(name: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _user.value = repository.loginUser(name, password)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message // Set error message
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.updateUser(user)
                _user.value = user
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteUser(user)
                _user.value = null
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message
            }
        }
    }

    fun fetchUserById(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _user.value = repository.getUserById(id)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message
            }
        }
    }
}
