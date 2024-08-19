package com.example.bankingapp.controller

import com.example.bankingapp.dto.AppointmentRequest
import com.example.bankingapp.exception.SlotAlreadyBookedException
import com.example.bankingapp.model.*
import com.example.bankingapp.repository.CustomerRepository
import com.example.bankingapp.repository.UserRepository
import com.example.bankingapp.service.AppointmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/appointments")
class AppointmentController(
    private val appointmentService: AppointmentService,
    private val userRepository: UserRepository,
    private val customerRepository: CustomerRepository
) {

    @PostMapping("/schedule")
    fun scheduleAppointment(@Valid @RequestBody request: AppointmentRequest): ResponseEntity<Any> {
        return try {
            // Get the logged-in user's username
            val principal = SecurityContextHolder.getContext().authentication.principal as org.springframework.security.core.userdetails.User
            val username = principal.username

            // Fetch the User entity associated with the logged-in user
            val user = userRepository.findByUsername(username)
                ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found")

            // Fetch the Customer entity associated with the User
            val customer = customerRepository.findByUser(user)
                ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Customer not found")

            // Ensure the logged-in customer is the one scheduling the appointment
            if (customer.id != request.customerId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only schedule appointments for your own account")
            }

            val appointments = appointmentService.scheduleAppointment(
                customer = customer,
                staffId = request.staffId,
                appointmentType = request.appointmentType,
                startTime = request.startTime,
                endTime = request.endTime,
                recurrenceType = request.recurrenceType,
                recurrenceEndDate = request.recurrenceEndDate
            )
            ResponseEntity.ok(appointments)
        } catch (e: SlotAlreadyBookedException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    @GetMapping("/staff")
    fun getAvailableStaff(@RequestParam role: String): ResponseEntity<List<BankStaff>> {
        val staff = appointmentService.getAvailableStaff(role)
        return ResponseEntity.ok(staff)
    }
}