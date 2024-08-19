// File: src/main/kotlin/com/example/bankingapp/repository/BankStaffRepository.kt
package com.example.bankingapp.repository

import com.example.bankingapp.model.BankStaff
import org.springframework.data.jpa.repository.JpaRepository

interface BankStaffRepository : JpaRepository<BankStaff, Long> {
    fun findByRole(role: String): List<BankStaff>
}
