package com.example.vaccination_project.db_connection.doctors

import java.sql.Connection
import java.sql.ResultSet
import java.sql.Types

/**
 * Provides an implementation of [DoctorsDAO] for performing database operations related to doctors.
 * This class allows CRUD (Create, Read, Update, Delete) operations on doctor entries within a database,
 * leveraging stored procedures and prepared statements to interact with the database.
 *
 * @property connection The database connection used for all SQL operations, assumed to be managed externally.
 */
class DBqueriesDoctors(private val connection: Connection) : DoctorsDAO {
    /**
     * Retrieves a doctor by their unique identifier.
     *
     * @param id The unique identifier of the doctor.
     * @return A [Doctors] instance representing the doctor, or null if no doctor is found with the given ID.
     */
    override fun getDoctorById(id: Int): Doctors? {
        val query = "{CALL getDoctorById(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToDoctor(resultSet)
        } else {
            null
        }
    }

    /**
     * Retrieves the unique identifier of a doctor based on their first name and surname.
     *
     * @param name The first name of the doctor.
     * @param surname The surname of the doctor.
     * @return The unique identifier of the doctor, or null if no matching doctor is found.
     */
    override fun getDoctorId(name: String, surname: String): Int? {
        val query = "{CALL getDoctorId(?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, name)
        callableStatement.setString(2, surname)
        callableStatement.registerOutParameter(3, Types.INTEGER)
        callableStatement.execute()

        return callableStatement.getInt(3)
    }

    /**
     * Retrieves a set of all doctors recorded in the database.
     *
     * @return A set of [Doctors] if any are found, or null if the database contains no doctor records.
     */
    override fun getAllDoctors(): Set<Doctors?>? {
        val query = "{CALL getAllDoctors()}"
        val callableStatement = connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()
        val doctors = mutableSetOf<Doctors?>()
        while (resultSet.next()) {
            doctors.add(mapResultSetToDoctor(resultSet))
        }
        return if (doctors.isEmpty()) null else doctors
    }

    /**
     * Inserts a new doctor into the database.
     *
     * @param doctor A [Doctors] instance containing the details of the doctor to be added.
     * @return True if the doctor was successfully added, false otherwise.
     */
    override fun insertDoctor(doctor: Doctors): Boolean {
        val preparedStatement = connection
            .prepareStatement("INSERT INTO Doctors (name, surname) VALUES (?, ?)")
        preparedStatement.setString(1, doctor.name)
        preparedStatement.setString(2, doctor.surname)

        val result = preparedStatement.executeUpdate() > 0
        preparedStatement.close()
        return result
    }

    /**
     * Updates the details of an existing doctor in the database.
     *
     * @param id The ID of the doctor to be updated.
     * @param doctor A [Doctors] instance containing the updated details of the doctor.
     * @return True if the doctor's details were successfully updated, false otherwise.
     */
    override fun updateDoctor(id: Int, doctor: Doctors): Boolean {
        val preparedStatement = connection
            .prepareStatement("UPDATE Doctors SET name = ?, surname = ? WHERE id = ?")
        preparedStatement.setString(1, doctor.name)
        preparedStatement.setString(2, doctor.surname)
        preparedStatement.setInt(3, id)

        return preparedStatement.executeUpdate() > 0
    }

    /**
     * Deletes a doctor from the database using their ID.
     *
     * @param id The unique identifier of the doctor to be deleted.
     * @return True if the doctor was successfully deleted, false otherwise.
     */
    override fun deleteDoctor(id: Int): Boolean {
        val preparedStatement = connection
            .prepareStatement("DELETE FROM Doctors WHERE id = ?")
        preparedStatement.setInt(1, id)

        return preparedStatement.executeUpdate() > 0
    }

    /**
     * Maps a [ResultSet] from a SQL query to a [Doctors] object.
     *
     * @param resultSet The [ResultSet] containing the SQL query results.
     * @return A [Doctors] object populated with data from the [ResultSet].
     */
    private fun mapResultSetToDoctor(resultSet: ResultSet): Doctors {
        return Doctors(
            id = resultSet.getInt("id"),
            name = resultSet.getString("name"),
            surname = resultSet.getString("surname")
        )
    }
}