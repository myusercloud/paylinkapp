package com.harry.pay.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pay.data.UserDatabase
import com.harry.pay.model.PaymentLink
import com.harry.pay.model.User
import com.harry.pay.repository.PaymentLinkRepository
import com.harry.pay.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UserRepository,
    context: Context // <-- Add context here
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _paymentLinks = MutableStateFlow<List<PaymentLink>>(emptyList())
    val paymentLinks: StateFlow<List<PaymentLink>> = _paymentLinks

    private val paymentLinkRepository =
        PaymentLinkRepository(UserDatabase.getDatabase(context).paymentLinkDao())

    init {
        fetchPaymentLinks()
    }

    private fun fetchPaymentLinks() {
        viewModelScope.launch {
            _paymentLinks.value = paymentLinkRepository.getAll()
        }
    }

    fun registerUser(user: User) = safeLaunch {
        repository.registerUser(user)
    }

    fun loginUser(name: String, password: String) = safeLaunch {
        _error.value = null
        _loginSuccess.value = false

        val user = repository.loginUser(name, password)
        if (user != null) {
            _user.value = user
            _loginSuccess.value = true
            fetchPaymentLinks() // refresh after login
        } else {
            _error.value = "Invalid username or password"
        }
    }

    fun updateUser(user: User) = safeLaunch {
        repository.updateUser(user)
        _user.value = user
    }

    fun deleteUser(user: User) = safeLaunch {
        repository.deleteUser(user)
        _user.value = null
    }

    fun fetchUserById(id: Int) = safeLaunch {
        _user.value = repository.getUserById(id)
    }

    fun clearError() {
        _error.value = null
    }

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
