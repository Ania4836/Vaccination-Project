package com.example.vaccination_project.db_connection.user

/**
 * Defines the data access object (DAO) for user management, specifying the methods necessary for interacting
 * with the user-related data stored in the database. This interface ensures standardized CRUD operations
 * (create, read, update, delete) are implemented for managing user information.
 *
 * Implementing classes should handle the necessary database connection and SQL operations, ensuring data
 * is properly mapped to and from the [Users] class.
 */
interface UsersDAO {

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user in the form of a String.
     * @return A [Users] object representing the user if found; null if no user is found with the provided identifier.
     */
    fun getUserid(userId: String): Users?

    /**
     * Retrieves all users currently stored in the database.
     *
     * @return A set containing [Users] objects for all users in the database, which could be empty if no users are found.
     */
    fun getAllUsers() : Set<Users?>?

    /**
     * Updates the details of an existing user in the database.
     *
     * @param userId The unique identifier of the user to update.
     * @param users The [Users] object containing the updated user information.
     * @return True if the update was successful, false otherwise.
     */
    fun updateUser (userId: String, users: Users): Boolean

    /**
     * Deletes a user from the database using their unique identifier.
     *
     * @param userId The unique identifier of the user to be deleted.
     * @return True if the user was successfully deleted, false otherwise.
     */
    fun deleteUser (userId: String): Boolean

    /**
     * Inserts a new user into the database.
     *
     * @param users A [Users] object containing the user's details to be inserted.
     * @return True if the user was successfully inserted, false otherwise.
     */
    fun insertUser (users: Users): Boolean

}

