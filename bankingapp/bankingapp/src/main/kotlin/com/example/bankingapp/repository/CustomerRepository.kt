package com.example.bankingapp.repository

import com.example.bankingapp.model.Customer
import com.example.bankingapp.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long>{
    fun findByCustomerId(customerId: String): Customer?
    fun findByUser(user: User): Customer?

}