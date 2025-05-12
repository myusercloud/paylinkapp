package com.harry.pay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pay.model.PaymentLink
import com.harry.pay.repository.PaymentLinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentLinkViewModel(private val repository: PaymentLinkRepository) : ViewModel() {

    private val _links = MutableStateFlow<List<PaymentLink>>(emptyList())
    val links: StateFlow<List<PaymentLink>> get() = _links

    init {
        observeLinks() // Start collecting data
    }

    private fun observeLinks() {
        viewModelScope.launch {
            repository.allLinks.collect { links ->
                _links.value = links
            }
        }
    }

    fun addLink(link: PaymentLink) {
        viewModelScope.launch {
            repository.insert(link)  // No need to manually load after adding
        }
    }

    fun updateLink(link: PaymentLink) {
        viewModelScope.launch {
            repository.update(link)
        }
    }

    fun deleteLink(link: PaymentLink) {
        viewModelScope.launch {
            repository.delete(link)
        }
    }
}
