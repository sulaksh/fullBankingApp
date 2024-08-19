
package com.example.bankingapp.dto

import com.example.bankingapp.model.AppointmentType
import java.time.LocalDateTime

enum class RecurrenceType {
    NONE, DAILY, WEEKLY, MONTHLY
}

data class AppointmentRequest(
    val customerId: Long,
    val staffId: Long,
    val appointmentType: AppointmentType,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val recurrenceType: RecurrenceType = RecurrenceType.NONE,
    val recurrenceEndDate: LocalDateTime? = null
)
