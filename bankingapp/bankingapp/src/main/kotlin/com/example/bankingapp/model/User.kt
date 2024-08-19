// File: src/main/kotlin/com/example/bankingapp/model/User.kt
package com.example.bankingapp.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*


@Entity
@Table(name = "app_user")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val roles: String,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    @JsonManagedReference
    var customer: Customer? = null
)
