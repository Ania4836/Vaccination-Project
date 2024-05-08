package com.example.vaccination_project.db_connection.schedule_date

interface ScheduleDateDAO {
    fun getScheduleDateById(id: Int) : ScheduleDate?
    fun getAllScheduleDates() : Set<ScheduleDate>?
    fun updateScheduleDate(id: Int, scheduleDate: ScheduleDate): Boolean
    fun deleteScheduleDate (id: Int): Boolean
    fun insertScheduleDate (scheduleDate: ScheduleDate): Boolean
}