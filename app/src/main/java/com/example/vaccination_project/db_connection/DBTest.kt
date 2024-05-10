package com.example.vaccination_project.db_connection

import com.example.vaccination_project.db_connection.doctors.DBqueriesDoctors
import com.example.vaccination_project.db_connection.doctors.Doctors
import com.example.vaccination_project.db_connection.schedule_date.DBqueriesScheduleDate
import com.example.vaccination_project.db_connection.schedule_date.ScheduleDate
import com.example.vaccination_project.db_connection.user.DBqueriesUsers
import com.example.vaccination_project.db_connection.user.Users
import com.example.vaccination_project.db_connection.vaccination.DBqueriesVaccination
import com.example.vaccination_project.db_connection.vaccination.Vaccination
import java.sql.Date
import java.sql.Time


/**
 * Demonstrates various database operations using the application's data access objects (DAOs).
 * This script initializes connections to the database and performs a series of CRUD operations
 * on different datasets, including users, vaccinations, doctors, and schedules.
 *
 * The process includes:
 * - Establishing a connection to the database.
 * - Creating instances of DAOs for users, vaccinations, doctors, and schedules.
 * - Performing insert, update, delete, and fetch operations on each dataset.
 * - Printing the results of these operations to standard output.
 * - Handling any exceptions that may occur during the operations.
 */
fun main() {
    try {
        val connection = DBconnection.getConnection()
        val scheduleQuery = DBqueriesScheduleDate(connection)
        val userQuery = DBqueriesUsers(connection)
        val doctorQuery = DBqueriesDoctors(connection)
        val vaccinationQuery = DBqueriesVaccination(connection)

        println("Testing insertUser():")
        val newUser = Users("13123929", "Jan", "Kowalski", Date.valueOf("1985-08-15"), "Male")
        println("Insertion successful:${userQuery.insertUser(newUser)}")

        println("Testing getAllUsers():")
        println(userQuery.getAllUsers())

        println("Testing insertVaccination():")
        val newVaccination = Vaccination(1, "Pfizer", Date.valueOf("2020-02-05"), Date.valueOf("2021-11-15"))
        println("Insertion successful: ${vaccinationQuery.insertVaccination(newVaccination)}")

        println("Testing getAllVaccines():")
        println(vaccinationQuery.getAllVaccinations())

        println("Testing insertSchedule():")
        val newScheduleDate = ScheduleDate(1, 1, "123", 1, Time.valueOf("12:30:00"), "Street Street",  Date.valueOf("2021-11-15"),1, 3)
        println("Insertion successful: ${scheduleQuery.insertScheduleDate(newScheduleDate)}")

        println("Testing getAllScheduleDates():")
        println(scheduleQuery.getAllScheduleDates())

        println("Testing insertDoctors():")
        val newDoctor = Doctors(1, "Jan", "Kowalski")
        println("Insertion successful: ${doctorQuery.insertDoctor(newDoctor)}")

        println("Testing getAllDoctors():")
        println(doctorQuery.getAllDoctors())

        println("Testing updateUser():")

        val updatedUser = Users( "23456", "Stece", "Jobs", Date.valueOf("1985-08-15"), "Male")
        println("Update successful:${userQuery.updateUser("23456", updatedUser)}")

        println("Testing deleteUser():")
        println("Deletion successful:${userQuery.deleteUser("23456")}")

        connection.close()
    }  catch (e: Exception) {
    e.printStackTrace()
    }
}




