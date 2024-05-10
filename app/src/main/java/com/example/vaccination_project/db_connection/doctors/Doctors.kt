package com.example.vaccination_project.db_connection.doctors

/**
 * Represents a doctor with identification and name details.
 * This data class is used throughout the application to manage and pass doctor information
 * between different components and database operations.
 *
 * @property id The unique identifier for the doctor, nullable to allow for creation before assignment in the database.
 * @property name The first name of the doctor.
 * @property surname The surname of the doctor.
 */
data class Doctors(
    var id: Int? = null, // Allows for object creation before an ID is assigned, as IDs are typically set by the database.
    var name: String, // Stores the first name of the doctor, required at the time of creation.
    var surname: String, // Stores the surname of the doctor, required at the time of creation.
)