package com.example.bankingapp.model

import jakarta.persistence.*

@Entity
@Table(name = "bank_staff")
data class BankStaff(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val role: String // Either 'Teller' or 'Manager'

    // Other fields as necessary
)
