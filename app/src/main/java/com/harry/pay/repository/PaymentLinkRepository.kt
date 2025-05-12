package com.harry.pay.repository

import com.harry.pay.data.PaymentLinkDao
import com.harry.pay.model.PaymentLink
import kotlinx.coroutines.flow.Flow

class PaymentLinkRepository(private val dao: PaymentLinkDao) {
    val allLinks: Flow<List<PaymentLink>> = dao.getAll() // Expose Flow directly

    suspend fun insert(link: PaymentLink) = dao.insert(link)
    suspend fun update(link: PaymentLink) = dao.update(link)
    suspend fun delete(link: PaymentLink) = dao.delete(link)
}

