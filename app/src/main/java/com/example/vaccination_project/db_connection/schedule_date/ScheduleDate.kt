package com.example.vaccination_project.db_connection.schedule_date

import java.sql.Date
import java.sql.Time

/**
 * Represents a scheduled vaccination date entry, detailing the appointment specifics.
 * This class is used to pass and manipulate scheduled vaccination data within the database
 * and across various components of the application.
 *
 * @property id The unique identifier for the scheduled date, nullable if not yet assigned (e.g., before insertion).
 * @property vaccineId The identifier for the vaccine associated with this appointment.
 * @property userId The identifier of the user who is scheduled to receive the vaccine.
 * @property doctorId The identifier of the doctor assigned to administer the vaccine.
 * @property scheduledTime The time at which the vaccine appointment is scheduled.
 * @property status The status of the appointment (e.g., 'Scheduled', 'Completed'), nullable if not specified.
 * @property scheduledDate The date on which the vaccine appointment is scheduled.
 * @property dose The specific dose number of the vaccine (e.g., 1st dose, 2nd dose).
 * @property intervalBetweenDoses The number of days between this and the next scheduled dose, if applicable.
 */
class ScheduleDate(
    val id: Int? = null, // Nullable to allow for creation before database assignment
    val vaccineId: Int, // Mandatory vaccine identifier.
    val userId: Int, // Mandatory user identifier.
    val doctorId: Int, // Mandatory doctor identifier.
    val scheduledTime: Time, // Mandatory scheduled time of the appointment.
    val status: String? = null, // Nullable status to allow flexibility in recording.
    val scheduledDate: Date, // Mandatory scheduled date of the appointment.
    val dose: Int,  // Mandatory indication of the dose number.
    val intervalBetweenDoses: Int // Mandatory days between doses, important for scheduling follow-ups.
)

