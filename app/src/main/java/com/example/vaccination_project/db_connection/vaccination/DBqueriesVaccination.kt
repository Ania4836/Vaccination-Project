package com.example.vaccination_project.db_connection.vaccination

import android.accounts.Account
import com.example.vaccination_project.db_connection.account.account
import java.sql.Connection
import java.sql.ResultSet

class DBqueriesVaccination(private val connection: Connection): VaccinationDAO {
    override fun getVaccination(name: String): vaccination? {
        val query = "{CALL getVaccination(?) }"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, vaccine_name)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccination(resultSet)
        } else {
            null
        }
    }

    override fun getAllVaccinations(): Set<vaccination>? {
        val query = "{CALL getVaccination()}"
        val callableStatement = connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()
        val skiers = mutableSetOf<Vaccination?>()
        while (resultSet.next()) {
            skiers.add(mapResultSetToVaccination(resultSet))
        }
        return if (skiers.isEmpty()) null else vaccination
    }

    override fun updateVaccination(name: String, vaccination: vaccination): Boolean {
        val query = "{CALL updateVaccination(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, vaccination.id)
        callableStatement.setString(2, vaccination.vaccine_name)
        callableStatement.setDate(3, vaccination.date_administered)
        callableStatement.setDate(4, vaccination.due_date)
        callableStatement.setDate(5, vaccination.next_dose_date)
        return callableStatement.executeUpdate() > 0
    }

    override fun deleteVaccination(name: String): Boolean {
        val query = "{CALL deleteVaccination(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, id)
        return callableStatement.executeUpdate() > 0
    }

    override fun insertVaccination(vaccination: Account): Boolean {
        val call = "{CALL insertAccount(?, ?, ?, ?, ?)}"
        val statement = connection.prepareCall(call)
        statement.setInt(1, vaccination.id)
        statement.setString(2, vaccination.vaccine_name)
        statement.setDate(3, vaccination.date_administered)
        statement.setDate(4, vaccination.due_date)
        statement.setDate(5, vaccination.next_dose_date)
        val result = !statement.execute()
        statement.close()
        return result
    }

    private fun mapResultSetToVaccination(resultSet: ResultSet): Vaccination? {
        return Vaccination(
            username = resultSet.getString("username"),
            email = resultSet.getString("email"),
            password = resultSet.getDate("password"),
        )
    }
}