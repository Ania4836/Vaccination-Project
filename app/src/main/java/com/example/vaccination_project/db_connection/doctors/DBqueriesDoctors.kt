package com.example.vaccination_project.db_connection.doctors

import java.sql.Connection
import java.sql.ResultSet
import java.sql.Types

class DBqueriesDoctors(private val connection: Connection) : DoctorsDAO {
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

    override fun getDoctorId(name: String, surname: String): Int? {
        val query = "{CALL getDoctorId(?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, name)
        callableStatement.setString(2, surname)
        callableStatement.registerOutParameter(3, Types.INTEGER)
        callableStatement.execute()

        return callableStatement.getInt(3)
    }

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

    override fun insertDoctor(doctor: Doctors): Boolean {
        val preparedStatement = connection
            .prepareStatement("INSERT INTO Doctors (name, surname) VALUES (?, ?)")
        preparedStatement.setString(1, doctor.name)
        preparedStatement.setString(2, doctor.surname)

        val result = preparedStatement.executeUpdate() > 0
        preparedStatement.close()
        return result
    }

    override fun updateDoctor(id: Int, doctor: Doctors): Boolean {
        val preparedStatement = connection
            .prepareStatement("UPDATE Doctors SET name = ?, surname = ? WHERE id = ?")
        preparedStatement.setString(1, doctor.name)
        preparedStatement.setString(2, doctor.surname)
        preparedStatement.setInt(3, id)

        return preparedStatement.executeUpdate() > 0
    }

    override fun deleteDoctor(id: Int): Boolean {
        val preparedStatement = connection
            .prepareStatement("DELETE FROM Doctors WHERE id = ?")
        preparedStatement.setInt(1, id)

        return preparedStatement.executeUpdate() > 0
    }

    private fun mapResultSetToDoctor(resultSet: ResultSet): Doctors {
        return Doctors(
            id = resultSet.getInt("id"),
            name = resultSet.getString("name"),
            surname = resultSet.getString("surname")
        )
    }
}