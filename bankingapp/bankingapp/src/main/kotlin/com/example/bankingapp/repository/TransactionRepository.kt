package com.example.bankingapp.repository

import com.example.bankingapp.model.Account
import com.example.bankingapp.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByAccountAndTimestampBetween(account: Account, startDate: LocalDateTime, endDate: LocalDateTime): List<Transaction>
}