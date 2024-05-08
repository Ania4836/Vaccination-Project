package com.example.vaccination_project.db_connection.vaccination

import java.sql.Date

data class Vaccination (
    val id: Int,
    var vaccineName: String,
    val dateAdministered: Date,
    val dueDate: Date,
    val nextDoseDate: Date
)