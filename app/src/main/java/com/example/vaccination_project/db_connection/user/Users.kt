package com.example.vaccination_project.db_connection.user

import java.sql.Date
class Users(
    val userId: Int,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: Date,
    val sex: String? = null
)
