package com.example.bankingapp.repository

import com.example.bankingapp.model.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long>{
    fun findByAccountNumber(accountNumber: String): Account?
}
