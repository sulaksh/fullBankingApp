// File: src/main/kotlin/com/example/bankingapp/controller/AuthController.kt
package com.example.bankingapp.controller

import com.example.bankingapp.model.Customer
import com.example.bankingapp.model.Role
import com.example.bankingapp.model.User
import com.example.bankingapp.repository.UserRepository
import com.example.bankingapp.service.CustomerService
import com.example.bankingapp.util.JwtTokenProvider
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val customerService: CustomerService
) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity.badRequest().body("Username is already taken!")
        }

        val user = User(
            username = signUpRequest.username,
            password = passwordEncoder.encode(signUpRequest.password),
            roles = Role.USER.name
        )

        val savedUser = userRepository.save(user)

        // Create and save the Customer entity associated with the User
        customerService.createCustomer(signUpRequest.name, savedUser)

        return ResponseEntity.ok("User registered successfully!")
    }

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtTokenProvider.generateJwtToken(authentication)
        println("User Logged in!")
        return ResponseEntity.ok(JwtResponse(jwt))
    }
}

data class SignUpRequest(
    val username: String,
    val password: String,
    val name: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class JwtResponse(val token: String)
