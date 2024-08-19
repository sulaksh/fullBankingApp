package com.example.bankingapp.service

import com.example.bankingapp.dto.RecurrenceType
import com.example.bankingapp.exception.SlotAlreadyBookedException
import com.example.bankingapp.model.*
import com.example.bankingapp.repository.AppointmentRepository
import com.example.bankingapp.repository.BankStaffRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class AppointmentService(
    private val appointmentRepository: AppointmentRepository,
    private val bankStaffRepository: BankStaffRepository
) {

    @Transactional
    fun scheduleAppointment(
        customer: Customer,
        staffId: Long,
        appointmentType: AppointmentType,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        recurrenceType: RecurrenceType,
        recurrenceEndDate: LocalDateTime?
    ): List<Appointment> {
        val now = LocalDateTime.now()

        if (startTime.isBefore(now)) {
            throw Exception("You cannot schedule an appointment in the past")
        }

        val staff = bankStaffRepository.findById(staffId).orElseThrow { Exception("Bank staff not found") }

        val appointments = mutableListOf<Appointment>()
        var currentStartTime = startTime
        var currentEndTime = endTime

        // Loop for recurring appointments or handle single appointment when recurrenceType is NONE
        do {
            // Check if the slot is already booked
            val overlappingAppointments = appointmentRepository.findOverlappingAppointments(staff, currentStartTime, currentEndTime)
            if (overlappingAppointments.isNotEmpty()) {
                throw SlotAlreadyBookedException("This slot is already booked, try booking another slot.")
            }

            // Schedule the appointment
            val appointment = Appointment(
                customer = customer,
                staff = staff,
                appointmentType = appointmentType,
                startTime = currentStartTime,
                endTime = currentEndTime,
                isBooked = true,
                recurrenceType = recurrenceType,
                recurrenceEndDate = recurrenceEndDate
            )
            appointments.add(appointmentRepository.save(appointment))

            // Move to the next recurrence (only if recurrenceType is not NONE)
            currentStartTime = when (recurrenceType) {
                RecurrenceType.DAILY -> currentStartTime.plus(1, ChronoUnit.DAYS)
                RecurrenceType.WEEKLY -> currentStartTime.plus(1, ChronoUnit.WEEKS)
                RecurrenceType.MONTHLY -> currentStartTime.plus(1, ChronoUnit.MONTHS)
                else -> currentStartTime // No change if NONE
            }
            currentEndTime = when (recurrenceType) {
                RecurrenceType.DAILY -> currentEndTime.plus(1, ChronoUnit.DAYS)
                RecurrenceType.WEEKLY -> currentEndTime.plus(1, ChronoUnit.WEEKS)
                RecurrenceType.MONTHLY -> currentEndTime.plus(1, ChronoUnit.MONTHS)
                else -> currentEndTime // No change if NONE
            }

        } while (recurrenceType != RecurrenceType.NONE && currentStartTime.isBefore(recurrenceEndDate))

        return appointments
    }

    fun getAvailableStaff(role: String): List<BankStaff> {
        return bankStaffRepository.findByRole(role)
    }
}