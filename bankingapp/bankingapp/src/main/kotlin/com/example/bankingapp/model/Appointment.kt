package com.example.bankingapp.model

import com.example.bankingapp.dto.RecurrenceType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "appointments")
data class Appointment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: Customer,

    @ManyToOne
    @JoinColumn(name = "staff_id")
    val staff: BankStaff,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val appointmentType: AppointmentType,

    @Column(nullable = false)
    val startTime: LocalDateTime,

    @Column(nullable = false)
    val endTime: LocalDateTime,

    @Column(nullable = false)
    val isBooked: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    val recurrenceType: RecurrenceType? = null,

    @Column(nullable = true)
    val recurrenceEndDate: LocalDateTime? = null,
)
