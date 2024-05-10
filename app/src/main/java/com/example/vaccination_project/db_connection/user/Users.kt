package com.example.vaccination_project.db_connection.user

/**
 * Represents a user in the system, encapsulating all necessary details about a user
 * as stored and retrieved from the database. This class is used throughout the application
 * to manage user information, including operations like CRUD actions in the database.
 *
 * @property userId The unique identifier for the user. This is a mandatory field.
 * @property firstName The user's first name. Nullable if the first name is not provided.
 * @property lastName The user's last name. Nullable if the last name is not provided.
 * @property dateOfBirth The user's date of birth. This is a mandatory field and used to calculate age or other criteria.
 * @property sex The user's sex. Nullable if the sex is not specified.
 */
import java.sql.Date
class Users(
    val userId: Int, // Mandatory unique identifier for each user.
    val firstName: String? = null, // Optional first name of the user.
    val lastName: String? = null, // Optional last name of the user.
    val dateOfBirth: Date, // Mandatory date of birth for age-related processing.
    val sex: String? = null // Optional sex of the user.
)
