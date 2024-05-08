package com.example.vaccination_project.db_connection.user

interface UsersDAO {
    fun getUserid(userId: String): Users?
    fun getAllUsers() : Set<Users?>?
    fun updateUser (userId: String, users: Users): Boolean
    fun deleteUser (userId: String): Boolean
    fun insertUser (users: Users): Boolean

}

