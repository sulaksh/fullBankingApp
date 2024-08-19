package com.example.bankingapp.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    val accountNumber: String = "",
    @Enumerated(EnumType.STRING)
    val accountType: AccountType = AccountType.SAVINGS,
    var balance: Double = 0.0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    val customer: Customer? = null,
    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val transactions: List<Transaction> = mutableListOf()
)
