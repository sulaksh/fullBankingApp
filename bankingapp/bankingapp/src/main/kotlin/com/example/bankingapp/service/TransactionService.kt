// File: src/main/kotlin/com/example/bankingapp/service/TransactionService.kt
package com.example.bankingapp.service

import com.example.bankingapp.model.Transaction
import com.example.bankingapp.model.TransactionType
import com.example.bankingapp.repository.AccountRepository
import com.example.bankingapp.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService(
    @Autowired private val transactionRepository: TransactionRepository,
    @Autowired private val accountRepository: AccountRepository
) {
    fun deposit(accountNumber: String, amount: Double): Transaction {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw Exception("Account not found")
        account.balance += amount
        accountRepository.save(account)
        val transaction = Transaction(account = account, amount = amount, transactionType = TransactionType.DEPOSIT)
        return transactionRepository.save(transaction)
    }

    fun withdraw(accountNumber: String, amount: Double): Transaction {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw Exception("Account not found")
        if (account.balance < amount) throw Exception("Insufficient balance")
        account.balance -= amount
        accountRepository.save(account)
        val transaction = Transaction(account = account, amount = -amount, transactionType = TransactionType.WITHDRAW)
        return transactionRepository.save(transaction)
    }

    fun transfer(fromAccountNumber: String, toAccountNumber: String, amount: Double): Transaction {
        val fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
            ?: throw Exception("From account not found")
        val toAccount = accountRepository.findByAccountNumber(toAccountNumber)
            ?: throw Exception("To account not found")
        if (fromAccount.balance < amount) throw Exception("Insufficient balance")
        fromAccount.balance -= amount
        toAccount.balance += amount
        accountRepository.save(fromAccount)
        accountRepository.save(toAccount)
        val transaction = Transaction(account = fromAccount, amount = -amount, transactionType = TransactionType.TRANSFER)
        transactionRepository.save(transaction)
        val transactionTo = Transaction(account = toAccount, amount = amount, transactionType = TransactionType.TRANSFER)
        return transactionRepository.save(transactionTo)
    }

    fun getTransactions(accountNumber: String, startDate: LocalDateTime, endDate: LocalDateTime): List<Transaction> {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw Exception("Account not found")
        return transactionRepository.findByAccountAndTimestampBetween(account, startDate, endDate)
    }
}
