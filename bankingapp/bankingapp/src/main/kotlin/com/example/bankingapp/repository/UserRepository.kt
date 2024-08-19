// File: src/main/kotlin/com/example/bankingapp/repository/UserRepository.kt
package com.example.bankingapp.repository

import com.example.bankingapp.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}
