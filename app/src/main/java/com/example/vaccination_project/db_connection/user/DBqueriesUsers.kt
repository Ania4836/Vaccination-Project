package com.example.vaccination_project.db_connection.user

import java.sql.Connection
import java.sql.ResultSet

/**
 * Provides database operations related to user management, interfacing with stored procedures to perform CRUD operations.
 * This class enables actions such as retrieving, updating, deleting, and inserting user records into the database.
 *
 * @property connection An active database connection used for all SQL operations, which should be managed externally.
 */
class DBqueriesUsers(private val connection: Connection) : UsersDAO {

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user.
     * @return A [Users] object representing the user if found, or null if no such user exists.
     */
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

    /**
     * Retrieves all users stored in the database.
     *
     * @return A set of [Users] objects, potentially empty if no users are found.
     */
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

    /**
     * Updates a specific user's details in the database.
     *
     * @param userId The unique identifier of the user to be updated.
     * @param users A [Users] object containing the updated details to store.
     * @return True if the update was successful, false otherwise.
     */
    override fun updateUser(userId: String, users: Users): Boolean {
        val query = "{CALL updateUser(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, users.userId)
        callableStatement.setString(2, users.firstName)
        callableStatement.setString(3, users.lastName)
        callableStatement.setDate(4, users.dateOfBirth)
        callableStatement.setString(5, users.sex)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Deletes a user from the database using their unique identifier.
     *
     * @param userId The unique identifier of the user to be deleted.
     * @return True if the deletion was successful, false otherwise.
     */
    override fun deleteUser(userId: String): Boolean {
        val query = "{CALL deleteUser(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, userId)
        return callableStatement.executeUpdate() > 0
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user A [Users] object containing the details of the new user.
     * @return True if the insertion was successful, false otherwise.
     */
    override fun insertUser(user: Users): Boolean {
        val call = "{CALL insertUser(?, ?, ?, ?, ?)}"
        val statement = connection.prepareCall(call)
        statement.setString(1, user.userId)
        statement.setString(2, user.firstName)
        statement.setString(3, user.lastName)
        statement.setDate(4, user.dateOfBirth)
        statement.setString(5, user.sex)
        val result = !statement.execute()
        statement.close()
        return result
    }

    /**
     * Maps a [ResultSet] from a SQL query to a [Users] object.
     *
     * @param resultSet The [ResultSet] containing the SQL query results.
     * @return A [Users] object populated with data from the [ResultSet].
     */
    private fun mapResultSetToUser(resultSet: ResultSet): Users? {
        return Users(
            userId = resultSet.getString("userId"),
            firstName = resultSet.getString("firstName"),
            lastName = resultSet.getString("lastName"),
            dateOfBirth = resultSet.getDate("dateOfBirth"),
            sex = resultSet.getString("sex"),
        )
    }
}