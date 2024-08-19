package com.example.bankingapp.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    val customerId: String = "",
    val name: String = "",
    @OneToMany(mappedBy = "customer", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val accounts: List<Account> = mutableListOf(),
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var user: User? = null

)
