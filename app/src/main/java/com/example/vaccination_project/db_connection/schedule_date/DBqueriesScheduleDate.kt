package com.example.vaccination_project.db_connection.schedule_date

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class DBqueriesScheduleDate(private val connection: Connection) : ScheduleDateDAO {

    override fun getScheduleDate(name: String): ScheduleDate? {
        val query = "{CALL getScheduleDate(?) }"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, status)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToScheduleDate(resultSet)
        } else {
            null
        }
    }

    override fun getAllScheduleDates(): Set<ScheduleDate>? {
        val query = "{CALL getScheduleDate()}"
        val callableStatement = connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()
        val skiers = mutableSetOf<ScheduleDate?>()
        while (resultSet.next()) {
            skiers.add(mapResultSetToScheduleDate(resultSet))
        }
        return if (skiers.isEmpty()) null else scheduledDate
    }

    override fun updateScheduleDate(name: String, scheduleDate: ScheduleDate): Boolean {
        val query = "{CALL updateScheduleDate(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, scheduleDate.status)
        callableStatement.setDate(2, scheduleDate.scheduledDate)
        callableStatement.setInt(3, scheduleDate.userId)
        callableStatement.setInt(4, scheduleDate.vaccineId)
        callableStatement.setTime(5, scheduleDate.time)

        return callableStatement.executeUpdate() > 0
    }

    override fun deleteScheduleDate(name: String): Boolean {
        val query = "{CALL deleteScheduleDate(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, status)
        return callableStatement.executeUpdate() > 0
    }


    override fun insertScheduleDate(user: ScheduleDate): Boolean {
        val call = "{CALL insertScheduleDate(?, ?, ?, ?, ?)}"
        val statement = connection.prepareCall(call)
        statement.setInt(1, scheduleDate.id)
        statement.setInt(2, scheduleDate.vaccineId)
        statement.setInt(3, scheduleDate.userId)
        statement.setInt(4, scheduleDate.doctorId)
        statement.setTime(5, scheduleDate.time)
        statement.setString(6, scheduleDate.status)
        statement.setDate(7, scheduleDate.scheduledDate)
        val result = !statement.execute()
        statement.close()
        return result
    }

    private fun mapResultSetToScheduleDate(resultSet: ResultSet): ScheduleDate? {
        return ScheduleDate(
            id = resultSet.getInt("id"),
            vaccineId = resultSet.getInt("vaccine_id"),
            status = resultSet.getString("status"),
            userId = resultSet.getInt("user_id"),
            doctorId = resultSet.getInt("doctors_id"),
            time = resultSet.getTime("time"),
            scheduledDate = resultSet.getDate("schedule_date"),
            dose = resultSet.getInt("dose")
        )
    }
}