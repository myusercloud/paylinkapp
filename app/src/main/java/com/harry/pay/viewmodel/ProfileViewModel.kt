package com.harry.pay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pay.repository.UserRepository
import com.harry.pay.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile

    fun loadUserProfile(userId: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            _userProfile.value = user
        }
    }
    fun deleteUserProfile(userId: Int) {
        viewModelScope.launch {
            userRepository.deleteUserById(userId)
        }
    }

}
