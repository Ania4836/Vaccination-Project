package com.example.vaccination_project.db_connection.user

import java.sql.Connection
import java.sql.ResultSet

class DBqueriesUsers(private val connection: Connection) : UsersDAO {

        override fun getUserid(userId: String): Users? {
        val query = "{CALL getUser(?) }"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, userId)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToUser(resultSet)
        } else {
            null
        }
    }

    override fun getAllUsers(): Set<Users?>? {
        val query = "{CALL getUsers()}"
        val callableStatement = connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()
        val users = mutableSetOf<Users?>()
        while (resultSet.next()) {
            users.add(mapResultSetToUser(resultSet))
        }
        return if (users.isEmpty()) null else users
    }

    override fun updateUser(userId: String, users: Users): Boolean {
        val query = "{CALL updateUser(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, users.userId)
        callableStatement.setString(2, users.firstName)
        callableStatement.setString(3, users.lastName)
        callableStatement.setDate(4, users.dateOfBirth)
        callableStatement.setString(5, users.sex)

        return callableStatement.executeUpdate() > 0
    }

    override fun deleteUser(userId: String): Boolean {
        val query = "{CALL deleteUser(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, userId)
        return callableStatement.executeUpdate() > 0
    }

    override fun insertUser(user: Users): Boolean {
        val call = "{CALL insertUser(?, ?, ?, ?, ?)}"
        val statement = connection.prepareCall(call)
        statement.setInt(1, user.userId)
        statement.setString(2, user.firstName)
        statement.setString(3, user.lastName)
        statement.setDate(4, user.dateOfBirth)
        statement.setString(5, user.sex)
        val result = !statement.execute()
        statement.close()
        return result
    }

    private fun mapResultSetToUser(resultSet: ResultSet): Users? {
        return Users(
            userId = resultSet.getInt("userId"),
            firstName = resultSet.getString("firstName"),
            lastName = resultSet.getString("lastName"),
            dateOfBirth = resultSet.getDate("dateOfBirth"),
            sex = resultSet.getString("sex"),
        )
    }
}