package com.example.vaccination_project.db_connection.schedule_date

import java.sql.Date
import java.sql.Time

class ScheduleDate(
    val id: Int? = null,
    val vaccineId: Int,
    val userId: Int,
    val doctorId: Int,
    val scheduledTime: Time,
    val status: String? = null,
    val scheduledDate: Date,
    val dose: Int,
    val intervalBetweenDoses: Int
)

