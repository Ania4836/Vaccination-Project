package com.example.vaccination_project.db_connection

import com.example.vaccination_project.db_connection.doctors.DBqueriesDoctors
import com.example.vaccination_project.db_connection.schedule_date.DBqueriesScheduleDate
import com.example.vaccination_project.db_connection.user.DBqueriesUsers
import com.example.vaccination_project.db_connection.vaccination.DBqueriesVaccination
import com.google.firebase.firestore.auth.User
import java.sql.Date

fun main() {
    try {
        val connection = DBconnection.getConnection()
        val appointmentQuery = DBqueriesScheduleDate(connection)
        val patientQuery = DBqueriesUsers(connection)
        val doctorQuery = DBqueriesDoctors(connection)
        val vaccineQuery = DBqueriesVaccination(connection)

        println("Testing insertUser():")
        val newUser = User("12345", "Jan", "Kowalski", Date.valueOf("1985-08-15"), "Male")
        println("Insertion successful:${dbQueries.insertUser(newUser)}")

        println("Testing getAllUsers():")
        println(dbQueries.getAllUsers())
        println("Testing insertUsers():")

        val newUser2 = User("23456", "Steve", "Jobs", Date.valueOf("1985-08-16"), "Male")
        println("Insertion successful:${dbQueries.insertSkier(newUser2)}")

        println("Testing updateUser():")

        val updatedUser = User(" 23456", "Stece", "Jobs", Date.valueOf("1985-08-15"), "Male")
        println("Update successful:${dbQueries.updateSkier("23456", updatedUser)}")
        println("Testing deleteSkier():")

        println("Deletion successful:${dbQueries.deleteUser("23456")}")

        connection.close()
    }  catch (e: Exception) {
    e.printStackTrace()
    }
}