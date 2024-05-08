package com.example.vaccination_project.db_connection.vaccination

import java.sql.Connection
import java.sql.ResultSet


class VaccinesQueries(private val connection : Connection) : VaccinesDAO {
    override fun getVaccineById(id: Int): Vaccines? {
        val query = "{CALL getVaccineById(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccine(resultSet)
        } else {
            null
        }
    }
class QueriesVaccination(private val connection: Connection): VaccinationDAO {

    override fun updateVaccination(name: String, vaccination: Vaccination): Boolean {
        val query = "{CALL updateVaccination(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, vaccination.id)
        callableStatement.setString(2, vaccination.vaccineName)
        callableStatement.setDate(3, vaccination.dateAdministered)
        callableStatement.setDate(4, vaccination.dueDate)
        callableStatement.setDate(5, vaccination.nextDoseDate)
        return callableStatement.executeUpdate() > 0
    }

    override fun deleteVaccination(name: String): Boolean {
        val preparedStatement = connection
            .prepareStatement("DELETE FROM Vaccines WHERE name = ?")
        preparedStatement.setString(1, name)

        return preparedStatement.executeUpdate() > 0
    }

    override fun getVaccinationById(name: String): Vaccination? {
        val query = "{CALL getVaccineById(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, name)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccination(resultSet)
        } else {
            null
        }
    }

    override fun getVaccinationByName(name: String): Vaccination? {
        val query = "{CALL getVaccineByName(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, name)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccination(resultSet)
        } else {
            null
        }
    }

    override fun getVaccineIdByVaccineName(vaccineName: String): Int {
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


    override fun getAppointmentsCountForVaccine(vaccineName: String): Int {
        val query = "{? = CALL getAppointmentsCountForVaccine(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.registerOutParameter(1, java.sql.Types.INTEGER)
        callableStatement.setString(2, vaccineName)
        callableStatement.execute()

        val totalAppointments = callableStatement.getInt(1)
        callableStatement.close()

        return totalAppointments
    }

    override fun insertVaccination(vaccination: Vaccination): Boolean {
        val call = "{CALL insertAccount(?, ?, ?, ?, ?)}"
        val statement = connection.prepareCall(call)
        statement.setInt(1, vaccination.id)
        statement.setString(2, vaccination.vaccineName)
        statement.setDate(3, vaccination.dateAdministered)
        statement.setDate(4, vaccination.dueDate)
        statement.setDate(5, vaccination.nextDoseDate)
        val result = !statement.execute()
        statement.close()

        return result
    }

    private fun mapResultSetToVaccination(resultSet: ResultSet): Vaccination {
        return Vaccination(
            id = resultSet.getInt("id"),
            vaccineName = resultSet.getString("vaccine_name"),
            dateAdministered = resultSet.getDate("password"),
            dueDate = resultSet.getDate("due_date"),
            nextDoseDate = resultSet.getDate("next_dose_date")
        )
    }
}