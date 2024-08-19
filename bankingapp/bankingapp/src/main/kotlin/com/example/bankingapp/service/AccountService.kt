// File: src/main/kotlin/com/example/bankingapp/service/AccountService.kt
package com.example.bankingapp.service

import com.example.bankingapp.model.Account
import com.example.bankingapp.model.AccountType
import com.example.bankingapp.repository.AccountRepository
import com.example.bankingapp.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountService(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val customerRepository: CustomerRepository
) {
    fun createAccount(customerId: String, accountType: AccountType): Account {
        val customer = customerRepository.findByCustomerId(customerId)
            ?: throw Exception("Customer not found")
        val accountNumber = generateAccountNumber()
        val account = Account(accountNumber = accountNumber, accountType = accountType, customer = customer)
        return accountRepository.save(account)
    }

    private fun generateAccountNumber(): String {
        return (10000000..99999999).random().toString()
    }
}
