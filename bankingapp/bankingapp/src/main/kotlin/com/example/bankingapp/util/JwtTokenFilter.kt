//package com.example.bankingapp.util
//
//import jakarta.servlet.FilterChain
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
//import org.springframework.stereotype.Component
//import org.springframework.web.filter.OncePerRequestFilter
//
//@Component
//class JwtTokenFilter(
//    private val jwtTokenProvider: JwtTokenProvider
//) : OncePerRequestFilter() {
//
//    override fun doFilterInternal(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        filterChain: FilterChain
//    ) {
//        val jwt = getJwt(request)
//        println("Inside JwtTokenFilter -> token from the request : $jwt")
//        if (jwt != null && jwtTokenProvider.validateJwtToken(jwt)) {
//            println("JWT token is valid")
//            val username = jwtTokenProvider.getUserNameFromJwtToken(jwt)
//            println("Username from JWT token: $username")
//            val userDetails = jwtTokenProvider.getUserDetailsService().loadUserByUsername(username)
//            println("User details loaded: $userDetails")
//
//            val authentication = JwtAuthenticationToken(userDetails, userDetails.authorities)
//            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
//            SecurityContextHolder.getContext().authentication = authentication
//            println("SecurityContextHolder set with authentication: $authentication")
//        } else {
//            println("JWT token is not valid or not present")
//        }
//        filterChain.doFilter(request, response)
//    }
//
//    private fun getJwt(request: HttpServletRequest): String? {
//        val authHeader = request.getHeader("Authorization")
//        println("Authorization Header: $authHeader")
//        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            authHeader.substring(7)
//        } else null
//    }
//}
package com.example.bankingapp.util

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = getJwt(request)
        println("Inside JwtTokenFilter -> token from the request : $jwt")
        if (jwt != null && jwtTokenProvider.validateJwtToken(jwt)) {
            println("JWT token is valid")
            val username = jwtTokenProvider.getUserNameFromJwtToken(jwt)
            println("Username from JWT token: $username")
            val userDetails = jwtTokenProvider.getUserDetailsService().loadUserByUsername(username)
            println("User details loaded: $userDetails")

            val authentication = JwtAuthenticationToken(userDetails, userDetails.authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            authentication.isAuthenticated = true  // Ensure the authentication is set to true
            SecurityContextHolder.getContext().authentication = authentication
            println("SecurityContextHolder set with authentication: $authentication")
        } else {
            println("JWT token is not valid or not present")
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwt(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")
        println("Authorization Header: $authHeader")
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else null
    }
}
