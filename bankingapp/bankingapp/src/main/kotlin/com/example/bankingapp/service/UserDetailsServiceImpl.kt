// File: src/main/kotlin/com/example/bankingapp/service/UserDetailsServiceImpl.kt
package com.example.bankingapp.service

import com.example.bankingapp.model.User
import com.example.bankingapp.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {

        println("Request received to fetch user details for user : $username")
        val user: User = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User Not Found with username: $username")

        val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority(user.roles))

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            authorities
        )
    }
}
