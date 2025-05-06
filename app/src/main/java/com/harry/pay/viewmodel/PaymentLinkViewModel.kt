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
        loadLinks()
    }

    private fun loadLinks() {
        viewModelScope.launch {
            _links.value = repository.getAll()
        }
    }

    fun addLink(link: PaymentLink) {
        viewModelScope.launch {
            repository.insert(link)
            loadLinks()
        }
    }

    fun updateLink(link: PaymentLink) {
        viewModelScope.launch {
            repository.update(link)
            loadLinks()
        }
    }

    fun deleteLink(link: PaymentLink) {
        viewModelScope.launch {
            repository.delete(link)
            loadLinks()
        }
    }
}
