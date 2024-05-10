package com.example.vaccination_project.db_connection.schedule_date

import java.sql.Date
import java.sql.Time

/**
 * Represents a scheduled vaccination date, encapsulating all relevant details about an appointment.
 * This class is utilized for managing and storing scheduling information related to vaccination events.
 *
 * @property id Optional unique identifier for the scheduled date, can be null if not assigned (e.g., before saving to a database).
 * @property vaccineId The identifier for the vaccine that is scheduled for administration.
 * @property userId The identifier for the user who is scheduled to receive the vaccine.
 * @property doctorId The identifier of the doctor who will administer the vaccine.
 * @property scheduledTime The specific time at which the vaccine is scheduled to be administered.
 * @property status Optional status of the vaccination appointment (e.g., "Scheduled", "Completed"), can be null if not specified.
 * @property scheduledDate The specific date on which the vaccine is scheduled to be administered.
 * @property dose The dose number of the vaccine that is scheduled, indicating if it is the first, second, or any subsequent dose.
 * @property intervalBetweenDoses The number of days between this scheduled vaccination and the next dose, if applicable.
 */
class ScheduleDate(
    val id: Int? = null,
    val vaccineId: Int,
    val userId: String,
    val doctorId: Int,
    val scheduledTime: Time,
    val status: String? = null,
    val scheduledDate: Date,
    val dose: Int,
    val intervalBetweenDoses: Int
)

