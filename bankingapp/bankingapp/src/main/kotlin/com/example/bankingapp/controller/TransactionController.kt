package com.example.bankingapp.controller

import com.example.bankingapp.model.Transaction
import com.example.bankingapp.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    @Autowired private val transactionService: TransactionService
) {

    @PostMapping("/deposit")
    fun deposit(@RequestBody request: TransactionRequest): Transaction {
        return transactionService.deposit(request.accountNumber, request.amount)
    }

    @PostMapping("/withdraw")
    fun withdraw(@RequestBody request: TransactionRequest): Transaction {
        return transactionService.withdraw(request.accountNumber, request.amount)
    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody request: TransferRequest): Transaction {
        return transactionService.transfer(request.fromAccountNumber, request.toAccountNumber, request.amount)
    }

    @GetMapping
    fun getTransactions(@RequestParam accountNumber: String, @RequestParam startDate: LocalDateTime, @RequestParam endDate: LocalDateTime): List<Transaction> {
        return transactionService.getTransactions(accountNumber, startDate, endDate)
    }
}

data class TransactionRequest(
    val accountNumber: String,
    val amount: Double
)

data class TransferRequest(
    val fromAccountNumber: String,
    val toAccountNumber: String,
    val amount: Double
)