package com.harry.pay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pay.repository.PaymentLinkRepository

class PaymentLinkViewModelFactory(
    private val repository: PaymentLinkRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentLinkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentLinkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
