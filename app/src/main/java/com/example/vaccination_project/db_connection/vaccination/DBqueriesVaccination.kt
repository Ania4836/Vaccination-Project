package com.example.vaccination_project.db_connection.vaccination

import java.sql.Connection
import java.sql.ResultSet

/**
 * Manages database operations for vaccinations, interfacing with stored procedures and SQL queries
 * to perform CRUD operations on vaccination records. This class ensures data is properly managed and
 * provides methods for retrieving, updating, deleting, and inserting vaccination data into the database.
 *
 * @property connection The active database connection used for all SQL operations, expected to be managed externally.
 */
class DBqueriesVaccination(private val connection : Connection) : VaccinationDAO {

    /**
     * Retrieves a vaccination record by its unique identifier.
     *
     * @param id The unique identifier of the vaccination.
     * @return A [Vaccination] object if found, or null if no such record exists.
     */
    override fun getVaccinationById(id: Int): Vaccination? {
        val query = "{CALL getVaccineById(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccination(resultSet)
        } else {
            null
        }
    }

    /**
     * Updates a vaccination record in the database.
     *
     * @param id The unique identifier of the vaccination to be updated.
     * @param vaccination A [Vaccination] object containing the updated details.
     * @return True if the update was successful, false otherwise.
     */
    override fun updateVaccination(id: Int, vaccination: Vaccination): Boolean {
        val query = "{CALL updateVaccination(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, vaccination.id)
        callableStatement.setString(2, vaccination.vaccineName)
        callableStatement.setDate(3, vaccination.dateAdministered)
        callableStatement.setDate(4, vaccination.dueDate)
        return callableStatement.executeUpdate() > 0
    }

    /**
     * Deletes a vaccination record from the database using its unique identifier.
     *
     * @param id The unique identifier of the vaccination to be deleted.
     * @return True if the deletion was successful, false otherwise.
     */
    override fun deleteVaccination(id: Int): Boolean {
        val preparedStatement = connection
            .prepareStatement("DELETE FROM Vaccination WHERE id = ?")
        preparedStatement.setInt(1, id)

        return preparedStatement.executeUpdate() > 0
    }

    /**
     * Retrieves all vaccination records from the database.
     *
     * @return A set of [Vaccination] objects, potentially empty if no records are found.
     */
    override fun getAllVaccinations(): Set<Vaccination?>? {
        val preparedStatement = connection.prepareStatement("SELECT * FROM Vaccination")
        val resultSet = preparedStatement.executeQuery()
        val vaccines = mutableSetOf<Vaccination?>()
        while (resultSet.next()) {
            vaccines.add(mapResultSetToVaccination(resultSet))
        }
        return if (vaccines.isEmpty()) {
            null
        } else {
            vaccines
        }
    }

    /**
     * Retrieves a vaccination record by the name of the vaccine.
     *
     * @param vaccineName The name of the vaccine.
     * @return A [Vaccination] object if found, or null if no such record exists.
     */
    override fun getVaccinationByName(vaccineName: String): Vaccination? {
        val query = "{CALL getVaccinationByName(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, vaccineName)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccination(resultSet)
        } else {
            null
        }
    }

    /**
     * Retrieves the unique identifier of a vaccination by the name of the vaccine.
     *
     * @param vaccineName The name of the vaccine.
     * @return The unique identifier of the vaccination, or 0 if no such record exists.
     */
    override fun getVaccinationIdByVaccineName(vaccineName: String): Int {
        val query = "{CALL getVaccineIdByVaccineName(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, vaccineName)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getInt(1)
        } else {
            0
        }
    }

    /**
     * Retrieves the count of doses for a specific vaccine by its name.
     *
     * @param vaccineName The name of the vaccine.
     * @return The count of doses administered for the vaccine.
     */
    override fun getDosesCountByVaccineName(vaccineName: String): Int {
        val query = "{CALL getDosesByVaccineName(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, vaccineName)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getInt(1)
        } else {
            0
        }
    }

    /**
     * Retrieves the count of appointments scheduled for a specific vaccine by its name.
     *
     * @param vaccineName The name of the vaccine.
     * @return The count of appointments scheduled for the vaccine.
     */
    override fun getAppointmentsCountForVaccination(vaccineName: String): Int {
        val query = "{? = CALL getAppointmentsCountForVaccine(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.registerOutParameter(1, java.sql.Types.INTEGER)
        callableStatement.setString(2, vaccineName)
        callableStatement.execute()

        val totalAppointments = callableStatement.getInt(1)
        callableStatement.close()

        return totalAppointments
    }

    /**
     * Inserts a new vaccination record into the database.
     *
     * @param vaccination A [Vaccination] object containing the details of the vaccination to be inserted.
     * @return True if the insertion was successful, false otherwise.
     */
    override fun insertVaccination(vaccination: Vaccination): Boolean {
        val call = "{CALL insertVaccination(?, ?, ?, ?, ?)}"
        val statement = connection.prepareCall(call)
        statement.setInt(1, vaccination.id)
        statement.setString(2, vaccination.vaccineName)
        statement.setDate(3, vaccination.dateAdministered)
        statement.setDate(4, vaccination.dueDate)
        val result = !statement.execute()
        statement.close()

        return result
    }

    /**
     * Maps a [ResultSet] from a SQL query to a [Vaccination] object.
     *
     * @param resultSet The [ResultSet] containing the SQL query results.
     * @return A [Vaccination] object populated with data from the [ResultSet].
     */
    private fun mapResultSetToVaccination(resultSet: ResultSet): Vaccination {
        return Vaccination(
            id = resultSet.getInt("id"),
            vaccineName = resultSet.getString("vaccineName"),
            dateAdministered = resultSet.getDate("dateAdministered"),
            dueDate = resultSet.getDate("dueDate"),
        )
    }
}

