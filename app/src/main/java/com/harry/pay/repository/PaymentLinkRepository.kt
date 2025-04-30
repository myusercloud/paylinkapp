package com.harry.pay.repository

import com.harry.pay.data.PaymentLinkDao
import com.harry.pay.model.PaymentLink

class PaymentLinkRepository(private val dao: PaymentLinkDao) {
    suspend fun insert(link: PaymentLink) = dao.insert(link)
    suspend fun getAll() = dao.getAll()
    suspend fun update(link: PaymentLink) = dao.update(link)
    suspend fun delete(link: PaymentLink) = dao.delete(link)
}
