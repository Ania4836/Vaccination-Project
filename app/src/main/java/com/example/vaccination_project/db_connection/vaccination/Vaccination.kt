package com.example.vaccination_project.db_connection.vaccination

import java.sql.Date

/**
 * Represents a record of a vaccination administered to a user, encapsulating all pertinent details.
 * This data class is used throughout the application to manage vaccination information,
 * including scheduling, tracking, and reporting vaccinations.
 *
 * @property id The unique identifier for the vaccination record. This is not nullable and must be provided.
 * @property vaccineName The name of the vaccine administered. This is crucial for identifying the type of vaccine.
 * @property dateAdministered The date on which the vaccine was administered. This is important for medical records
 * and determining the schedule for subsequent doses.
 * @property dueDate The date by which the next dose of the vaccine (if applicable) is due. This helps in scheduling
 * and alerting users for their upcoming vaccinations.
 */
data class Vaccination (
    var id: Int, // Unique identifier for each vaccination record.
    var vaccineName: String, // Name of the vaccine administered.
    val dateAdministered: Date, // Date when the vaccine was administered.
    val dueDate: Date, // Date by which the next dose is due (if applicable).
)
