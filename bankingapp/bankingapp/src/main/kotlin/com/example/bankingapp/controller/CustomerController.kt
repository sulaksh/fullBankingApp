// File: src/main/kotlin/com/example/bankingapp/controller/CustomerController.kt
package com.example.bankingapp.controller

import com.example.bankingapp.model.Customer
import com.example.bankingapp.model.User
import com.example.bankingapp.repository.UserRepository
import com.example.bankingapp.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    @Autowired private val customerService: CustomerService,
    @Autowired private val userRepository: UserRepository
) {

    @PostMapping
    fun createCustomer(@RequestBody customer: Customer): Customer {
        // Get the logged-in user's username
        val principal = SecurityContextHolder.getContext().authentication.principal as User
        val username = principal.username

        // Find the User entity by username
        val userEntity = userRepository.findByUsername(username)
            ?: throw Exception("User not found")

        // Create the Customer linked to the authenticated User
        return customerService.createCustomer(customer.name, userEntity)
    }

    @GetMapping
    fun getAllCustomers(): List<Customer> {
        return customerService.getAllCustomers()
    }

    @GetMapping("/{customerId}")
    fun getCustomerById(@PathVariable customerId: String): ResponseEntity<Customer> {
        val customer = customerService.findCustomerById(customerId)
        return if (customer != null) {
            ResponseEntity.ok(customer)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}