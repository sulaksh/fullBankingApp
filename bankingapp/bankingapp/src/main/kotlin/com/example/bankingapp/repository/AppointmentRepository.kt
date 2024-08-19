// File: src/main/kotlin/com/example/bankingapp/repository/AppointmentRepository.kt
package com.example.bankingapp.repository

import com.example.bankingapp.model.Appointment
import com.example.bankingapp.model.BankStaff
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface AppointmentRepository : JpaRepository<Appointment, Long> {
//    fun findByStaffAndStartTimeBetween(staff: BankStaff, startTime: LocalDateTime, endTime: LocalDateTime): List<Appointment>
@Query("""
        SELECT a FROM Appointment a 
        WHERE a.staff = :staff 
        AND a.startTime < :endTime 
        AND a.endTime > :startTime
    """)
fun findOverlappingAppointments(
    @Param("staff") staff: BankStaff,
    @Param("startTime") startTime: LocalDateTime,
    @Param("endTime") endTime: LocalDateTime
): List<Appointment>
}
