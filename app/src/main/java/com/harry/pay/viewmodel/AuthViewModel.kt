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

    fun registerUser(user: User) {
        viewModelScope.launch {
            repository.registerUser(user)
        }
    }

    fun loginUser(name: String, password: String) {
        viewModelScope.launch {
            _user.value = repository.loginUser(name, password)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
            _user.value = user
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
            _user.value = null
        }
    }

    fun fetchUserById(id: Int) {
        viewModelScope.launch {
            _user.value = repository.getUserById(id)
        }
    }
}