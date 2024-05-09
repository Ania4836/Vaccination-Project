package com.example.vaccination_project.db_connection.vaccination

import java.sql.Connection
import java.sql.ResultSet

class DBqueriesVaccination(private val connection : Connection) : VaccinationDAO {
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

    override fun updateVaccination(id: Int, vaccination: Vaccination): Boolean {
        val query = "{CALL updateVaccination(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, vaccination.id)
        callableStatement.setString(2, vaccination.vaccineName)
        callableStatement.setDate(3, vaccination.dateAdministered)
        callableStatement.setDate(4, vaccination.dueDate)
        callableStatement.setDate(5, vaccination.nextDoseDate)
        return callableStatement.executeUpdate() > 0
    }

    override fun deleteVaccination(id: Int): Boolean {
        val preparedStatement = connection
            .prepareStatement("DELETE FROM Vaccination WHERE id = ?")
        preparedStatement.setInt(1, id)

        return preparedStatement.executeUpdate() > 0
    }

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

    override fun insertVaccination(vaccination: Vaccination): Boolean {
        val call = "{CALL insertVaccination(?, ?, ?, ?, ?)}"
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
            vaccineName = resultSet.getString("vaccineName"),
            dateAdministered = resultSet.getDate("dateAdministered"),
            dueDate = resultSet.getDate("dueDate"),
            nextDoseDate = resultSet.getDate("nextDoseDate")
        )
    }
}

