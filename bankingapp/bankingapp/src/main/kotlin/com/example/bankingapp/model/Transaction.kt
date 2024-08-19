package com.example.bankingapp.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonBackReference
    val account: Account? = null,
    val amount: Double = 0.0,
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType = TransactionType.DEPOSIT,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
