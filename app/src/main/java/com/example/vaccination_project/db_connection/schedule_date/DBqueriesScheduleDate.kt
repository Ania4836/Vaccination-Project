package com.example.vaccination_project.db_connection.schedule_date

import java.sql.Connection
import java.sql.ResultSet

/**
 * Handles database operations related to scheduling dates for vaccinations.
 * This class provides CRUD (Create, Read, Update, Delete) functionality through stored procedures,
 * allowing manipulation and retrieval of scheduled date records within the database.
 *
 * @property connection The active database connection used for all SQL operations, expected to be managed externally.
 */
class DBqueriesScheduleDate(private val connection: Connection) : ScheduleDateDAO {

    /**
     * Retrieves a scheduled date by its unique identifier.
     *
     * @param id The unique identifier of the scheduled date entry.
     * @return A [ScheduleDate] object if found, or null if no entry exists with the given ID.
     */
    override fun getScheduleDateById(id: Int): ScheduleDate? {
        val query = "{CALL getScheduleDate(?) }"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToScheduleDate(resultSet)
        } else {
            null
        }
    }

    /**
     * Retrieves all scheduled dates from the database.
     *
     * @return A set of [ScheduleDate] objects, potentially empty if no dates are scheduled.
     */
    override fun getAllScheduleDates(): Set<ScheduleDate?>? {
        val query = "{CALL getScheduleDate()}"
        val callableStatement = connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()
        val scheduleDate = mutableSetOf<ScheduleDate?>()
        while (resultSet.next()) {
            scheduleDate.add(mapResultSetToScheduleDate(resultSet))
        }
        return if (scheduleDate.isEmpty()) null else scheduleDate
    }

    /**
     * Updates a specific scheduled date entry in the database.
     *
     * @param id The ID of the scheduled date to update.
     * @param scheduleDate A [ScheduleDate] object containing the updated details to be stored.
     * @return True if the update was successful, false otherwise.
     */
    override fun updateScheduleDate(id: Int, scheduleDate: ScheduleDate): Boolean {
        val query = "{CALL updateScheduleDate(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, id)
        callableStatement.setInt(2, scheduleDate.vaccineId ?: 0)
        callableStatement.setInt(3, scheduleDate.userId)
        callableStatement.setInt(4, scheduleDate.doctorId ?: 0)
        callableStatement.setDate(5, scheduleDate.scheduledDate)
        callableStatement.setTime(6, scheduleDate.scheduledTime)
        callableStatement.setString(7, scheduleDate.status)
        callableStatement.setInt(8, scheduleDate.dose ?: 0)
        callableStatement.setInt(9, scheduleDate.intervalBetweenDoses)
        return callableStatement.executeUpdate() > 0
    }

    /**
     * Deletes a scheduled date from the database using its ID.
     *
     * @param id The unique identifier of the scheduled date to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    override fun deleteScheduleDate(id: Int): Boolean {
        val query = "{CALL deleteScheduleDate(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, id)
        return callableStatement.executeUpdate() > 0
    }

    /**
     * Inserts a new scheduled date into the database.
     *
     * @param scheduleDate A [ScheduleDate] object containing the details of the new appointment.
     * @return True if the insertion was successful, false otherwise.
     */
    override fun insertScheduleDate(scheduleDate: ScheduleDate): Boolean {
        val call = "{CALL insertScheduleDate(?, ?, ?, ?, ?, ?, ?)}"
        val statement = connection.prepareCall(call)
        statement.setInt(1, scheduleDate.vaccineId)
        statement.setInt(2, scheduleDate.userId)
        statement.setInt(3, scheduleDate.doctorId)
        statement.setDate(4, scheduleDate.scheduledDate)
        statement.setTime(5, scheduleDate.scheduledTime)
        statement.setString(6, scheduleDate.status)
        statement.setInt(7, scheduleDate.dose)

        val result = !statement.execute()
        statement.close()
        return result
    }

    /**
     * Maps a [ResultSet] from a SQL query to a [ScheduleDate] object.
     *
     * @param resultSet The [ResultSet] containing the SQL query results.
     * @return A [ScheduleDate] object populated with data from the [ResultSet].
     */
    private fun mapResultSetToScheduleDate(resultSet: ResultSet): ScheduleDate {
        return ScheduleDate(
            id = resultSet.getInt("id"),
            vaccineId = resultSet.getInt("vaccineId"),
            userId = resultSet.getInt("userId"),
            doctorId = resultSet.getInt("doctorId"),
            scheduledDate = resultSet.getDate("scheduledDate"),
            scheduledTime = resultSet.getTime("scheduledTime"),
            status = resultSet.getString("status"),
            dose = resultSet.getInt("dose"),
            intervalBetweenDoses = resultSet.getInt("intervalBetweenDoses")
        )
    }
}