package com.example.vaccination_project.db_connection.schedule_date

import java.sql.Connection
import java.sql.ResultSet

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
        }    }

    override fun getAllScheduleDates(): Set<ScheduleDate>? {
        val query = "{CALL getScheduleDate()}"
        val callableStatement = connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()
        val skiers = mutableSetOf<ScheduleDate?>()
        while (resultSet.next()) {
            skiers.add(mapResultSetToScheduleDate(resultSet))
        }
        return if (skiers.isEmpty()) null else schedule_date    }

    override fun updateScheduleDate(name: String, scheduleDate: ScheduleDate): Boolean {
        val query = "{CALL updateScheduleDate(?, ?, ?, ?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, scheduleDate.status)
        callableStatement.setString(2, scheduleDate.schedule_date)
        callableStatement.setDate(3, scheduleDate.user_id)
        callableStatement.setDate(4, scheduleDate.vaccine_name)
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
        statement.setString(1, scheduleDate.status)
        statement.setString(2, scheduleDate.schedule_date)
        statement.setDate(3, scheduleDate.user_id)
        statement.setDate(4, scheduleDate.vaccine_name)
        val result = !statement.execute()
        statement.close()
        return result    }

    private fun mapResultSetToScheduleDate(resultSet: ResultSet): ScheduleDate? {
        return ScheduleDate(
            status = resultSet.getString("status"),
            schedule_date = resultSet.getString("schedule_date"),
            user_id = resultSet.getDate("user_id"),
            vaccine_name = resultSet.getDate("vaccine_name"),
        )
    }
}