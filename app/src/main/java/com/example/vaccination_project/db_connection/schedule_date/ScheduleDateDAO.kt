package com.example.vaccination_project.db_connection.schedule_date

/**
 * Defines the data access operations for managing scheduled vaccination dates within the database.
 * This interface specifies methods for creating, reading, updating, and deleting scheduled date entries,
 * ensuring standardized interaction with the database for scheduling related tasks.
 *
 * Implementing classes should handle the necessary database connection and execution of SQL queries or stored procedures,
 * transforming the data to and from [ScheduleDate] objects.
 */
interface ScheduleDateDAO {

    /**
     * Retrieves a scheduled date by its unique identifier.
     *
     * @param id The unique identifier of the scheduled date.
     * @return A [ScheduleDate] object representing the scheduled appointment, or null if no such appointment exists.
     */
    fun getScheduleDateById(id: Int) : ScheduleDate?

    /**
     * Retrieves all scheduled dates currently stored in the database.
     *
     * @return A set of [ScheduleDate] objects, each representing a scheduled appointment; returns null or an empty set if no appointments are found.
     */
    fun getAllScheduleDates() : Set<ScheduleDate?>?

    /**
     * Updates the details of an existing scheduled date in the database.
     *
     * @param id The unique identifier of the scheduled date to be updated.
     * @param scheduleDate A [ScheduleDate] object containing the updated details.
     * @return True if the update was successful, false otherwise.
     */
    fun updateScheduleDate(id: Int, scheduleDate: ScheduleDate): Boolean

    /**
     * Deletes a scheduled date from the database using its unique identifier.
     *
     * @param id The unique identifier of the scheduled date to be deleted.
     * @return True if the deletion was successful, false otherwise.
     */
    fun deleteScheduleDate (id: Int): Boolean

    /**
     * Inserts a new scheduled date into the database.
     *
     * @param scheduleDate A [ScheduleDate] object containing the details of the new appointment.
     * @return True if the insertion was successful, false otherwise.
     */
    fun insertScheduleDate (scheduleDate: ScheduleDate): Boolean
}