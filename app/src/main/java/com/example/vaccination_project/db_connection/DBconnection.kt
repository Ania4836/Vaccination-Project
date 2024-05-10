package com.example.vaccination_project.db_connection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Manages database connections for the application, encapsulating the details required to establish connections with the database.
 * This object provides a centralized way to manage and retrieve database connections using JDBC.
 *
 * The database parameters such as URL, user, and password are predefined. This object also ensures that the MySQL JDBC driver
 * is loaded properly before any connection attempts are made.
 */
object DBconnection {
    private const val URL = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11696934?useUnicode=true&characterEncoding=utf-8&serverTimezone=CET"
    private const val USER = "sql11696934"
    private const val PASS = "bccJc8WYr5"

    /**
     * Initializes the necessary database driver class.
     */
    init {
        Class.forName("com.mysql.jdbc.Driver")
    }

    /**
     * Establishes a connection to the database using predefined credentials and configuration settings.
     *
     * @return A [Connection] object to interact with the database.
     * @throws RuntimeException If there is an error establishing the connection, encapsulating the original [SQLException].
     */
    suspend fun getConnection(): Connection {
        return withContext(Dispatchers.IO) {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                DriverManager.getConnection(URL, USER, PASS)
            } catch (e: Exception) {
                throw RuntimeException("Error connecting to the database", e)
            }
        }
    }

    /**
     * Main method for testing the connection. This method attempts to open and then close a database connection.
     *
     * @param args Not used.
     */
    @JvmStatic
    suspend fun main(args: Array<String>) {
        try {
            val conn = getConnection()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}