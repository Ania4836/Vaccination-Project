package com.example.vaccination_project.db_connection

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Provides a demonstration of various database operations using JDBC to connect to a MySQL server.
 * This object includes functions to perform CRUD operations (Create, Read, Update, Delete) on a vaccination schedule database,
 * as well as utility functions to manipulate and query the database schema. The main method orchestrates multiple database
 * operations to show practical usage of JDBC in handling real-world scenarios.
 *
 * The operations include:
 * - Establishing a connection to a predefined MySQL database.
 * - Adding, updating, deleting, and retrieving vaccination records.
 * - Checking for the existence of and adding new columns to tables.
 * - Displaying the database tables and their content.
 */
object TestFreeSQLServer {

    /**
     * Main entry point of the demonstration. Initializes the database connection, and executes a series of database operations.
     * Each operation is printed to the console, allowing for a step-by-step observation of outcomes.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        val urlSB = StringBuilder("jdbc:mysql://")
        urlSB.append("sql11.freesqldatabase.com:3306/")
        urlSB.append("sql11696934?")
        urlSB.append("useUnicode=true&characterEncoding=utf-8")
        urlSB.append("&user=sql11696934")
        urlSB.append("&password=bccJc8WYr5")
        urlSB.append("&serverTimezone=CET")
        val connectionUrl = urlSB.toString()
        try {
            val conn = DriverManager.getConnection(connectionUrl)

            // Adding a new record to the vaccination schedule
            addNewVaccinationRecord(conn, "John Doe", "2024-04-05", "Pfizer", "City Hospital")

            // Deleting a record from the vaccination schedule
            deleteVaccinationRecord(conn, "John Doe")

            // Retrieving vaccination data for a specific patient
            retrievePatientVaccinationData(conn, "John Doe")

            // Updating vaccination data
            updateVaccinationData(conn, "John Doe", "Moderna")

            // Checking and adding a new column
            checkAndAddColumn(conn, "vaccination_schedule", "vaccination_time")

            // Displaying tables in the database
            displayTables(conn)
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }


    private fun addNewVaccinationRecord(
        conn: Connection,
        patientName: String,
        vaccinationDate: String,
        vaccineType: String,
        vaccinationLocation: String
    ) {
        try {
            val insertStatement = conn.prepareStatement(
                "INSERT INTO `vaccination_schedule`(`patient_name`, `vaccination_date`, `vaccine_type`, `vaccination_location`) VALUES (?,?,?,?)"
            )
            insertStatement.setString(1, patientName)
            insertStatement.setString(2, vaccinationDate)
            insertStatement.setString(3, vaccineType)
            insertStatement.setString(4, vaccinationLocation)

            val rowsAffected = insertStatement.executeUpdate()
            if (rowsAffected > 0) {
                println("New vaccination record has been added successfully.")
            } else {
                println("Failed to add a new vaccination record.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }


    private fun deleteVaccinationRecord(conn: Connection, patientName: String) {
        try {
            val deleteStatement = conn.prepareStatement(
                "DELETE FROM `vaccination_schedule` WHERE `patient_name` = ?"
            )
            deleteStatement.setString(1, patientName)

            val rowsAffected = deleteStatement.executeUpdate()
            if (rowsAffected > 0) {
                println("Vaccination record for $patientName has been deleted successfully.")
            } else {
                println("No vaccination record found for $patientName.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun retrievePatientVaccinationData(conn: Connection, patientName: String) {
        try {
            val selectQuery = "SELECT * FROM `vaccination_schedule` WHERE `patient_name` = ?"
            val selectStatement = conn.prepareStatement(selectQuery)
            selectStatement.setString(1, patientName)

            val resultSet = selectStatement.executeQuery()
            println("Vaccination records for $patientName:")
            printResultSet(resultSet)
            resultSet.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
    private fun updateVaccinationData(conn: Connection, patientName: String, newVaccineType: String) {
        try {
            val updateStatement = conn.prepareStatement(
                "UPDATE `vaccination_schedule` SET `vaccine_type` = ? WHERE `patient_name` = ?"
            )
            updateStatement.setString(1, newVaccineType)
            updateStatement.setString(2, patientName)

            val rowsAffected = updateStatement.executeUpdate()
            if (rowsAffected > 0) {
                println("Vaccine type for $patientName has been updated successfully.")
            } else {
                println("Failed to update vaccine type for $patientName.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
    private fun checkAndAddColumn(conn: Connection, table: String, newColumn: String) {
        try {
            val metaData = conn.metaData
            val resultSet = metaData.getColumns(null, null, table, newColumn)
            if (resultSet.next()) {
                println("Column $newColumn already exists in table $table.")
            } else {
                val alterTableQuery = "ALTER TABLE $table ADD COLUMN $newColumn VARCHAR(255)"
                val statement = conn.createStatement()
                statement.executeUpdate(alterTableQuery)
                println("New column $newColumn has been successfully added to table $table.")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
    private fun displayTables(conn: Connection) {
        try {
            println("Tables:")
            val showTablesST = conn.prepareStatement("SHOW TABLES")
            val rs1 = showTablesST.executeQuery()
            while (rs1.next()) {
                val s = rs1.getString(1)
                print("$s ")
            }
            println("")

            println("*************** vaccination_schedule ***************")
            val selectAllSt = conn.prepareStatement("SELECT * FROM vaccination_schedule;")
            val rsAllSt = selectAllSt.executeQuery()
            printResultSet(rsAllSt)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    @Throws(SQLException::class)
    private fun printResultSet(resultSet: ResultSet) {
        val rsmd = resultSet.metaData
        val columnsNumber = rsmd.columnCount
        while (resultSet.next()) {
            for (i in 1..columnsNumber) {
                if (i > 1) print(", ")
                val columnValue = resultSet.getString(i)
                print(rsmd.getColumnName(i) + ": " + columnValue)
            }
            println("")
        }
        println("")
    }
}
