package com.example.vaccination_project.db_connection.schedule_date


import java.sql.Connection
import java.sql.ResultSet

class DBqueriesScheduleDate(private val connection: Connection) : ScheduleDateDAO {

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
        return callableStatement.executeUpdate() > 0
    }

    override fun deleteScheduleDate(id: Int): Boolean {
        val query = "{CALL deleteScheduleDate(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, id)
        return callableStatement.executeUpdate() > 0
    }

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
        return result    }

    private fun mapResultSetToScheduleDate(resultSet: ResultSet): ScheduleDate {
        return ScheduleDate(
            id = resultSet.getInt("id"),
            vaccineId = resultSet.getInt("vaccine_id"),
            userId = resultSet.getInt("user_id"),
            doctorId = resultSet.getInt("doctor_id"),
            scheduledDate = resultSet.getDate("scheduled_date"),
            scheduledTime = resultSet.getTime("scheduled_time"),
            status = resultSet.getString("status"),
            dose = resultSet.getInt("dose")
        )
    }
}