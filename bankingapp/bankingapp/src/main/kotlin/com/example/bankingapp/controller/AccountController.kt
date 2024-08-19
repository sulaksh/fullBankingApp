// File: src/main/kotlin/com/example/bankingapp/controller/AccountController.kt
package com.example.bankingapp.controller

import com.example.bankingapp.model.Account
import com.example.bankingapp.model.AccountType
import com.example.bankingapp.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    @Autowired private val accountService: AccountService
) {

    @PostMapping
    fun createAccount(@RequestParam customerId: String, @RequestParam accountType: AccountType): Account {
        return accountService.createAccount(customerId, accountType)
    }


}
