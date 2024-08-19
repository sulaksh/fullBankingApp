//// File: src/main/kotlin/com/example/bankingapp/util/JwtTokenProvider.kt
//package com.example.bankingapp.util
//
//import com.example.bankingapp.service.UserDetailsServiceImpl
//import io.jsonwebtoken.Claims
//import io.jsonwebtoken.Jwts
//import io.jsonwebtoken.SignatureAlgorithm
//import io.jsonwebtoken.io.Decoders
//import io.jsonwebtoken.security.Keys
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.stereotype.Component
//import java.security.Key
//import java.util.*
//
//@Component
//class JwtTokenProvider(
//    @Value("\${app.jwtSecret}") private val jwtSecret: String,
//    @Value("\${app.jwtExpirationMs}") private val jwtExpirationMs: Int,
//    private val userDetailsService: UserDetailsServiceImpl
//) {
//
//    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
//
//    fun generateJwtToken(authentication: Authentication): String {
//        val userPrincipal = authentication.principal as UserDetails
//        return Jwts.builder()
//            .setSubject(userPrincipal.username)
//            .setIssuedAt(Date())
//            .setExpiration(Date(Date().time + jwtExpirationMs))
//            .signWith(key, SignatureAlgorithm.HS512)
//            .compact()
//    }
//
//    fun getUserNameFromJwtToken(token: String): String {
//        val claims: Claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
//        return claims.subject
//    }
//
//    fun validateJwtToken(authToken: String): Boolean {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
//            return true
//        } catch (e: Exception) {
//            println("Invalid JWT token: ${e.message}")
//        }
//        return false
//    }
//
//    fun getUserDetailsService(): UserDetailsServiceImpl {
//        return userDetailsService
//    }
//}
package com.example.bankingapp.util

import com.example.bankingapp.service.UserDetailsServiceImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${app.jwtSecret}") private val jwtSecret: String,
    @Value("\${app.jwtExpirationMs}") private val jwtExpirationMs: Int,
    private val userDetailsService: UserDetailsServiceImpl
) {

    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetails
        val token = Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
        println("Generated JWT token: $token")
        return token
    }

    fun getUserNameFromJwtToken(token: String): String {
        val claims: Claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        println("Extracted username from JWT token: ${claims.subject}")
        return claims.subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            println("JWT token is valid")
            true
        } catch (e: Exception) {
            println("Invalid JWT token: ${e.message}")
            false
        }
    }

    fun getUserDetailsService(): UserDetailsServiceImpl {
        return userDetailsService
    }
}
