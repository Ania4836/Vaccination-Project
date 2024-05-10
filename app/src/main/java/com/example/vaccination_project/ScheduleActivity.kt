package com.example.vaccination_project

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.vaccination_project.db_connection.DBconnection
import kotlinx.coroutines.*
import java.sql.Connection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * An activity that facilitates scheduling of vaccination appointments. It provides interfaces to select dates and times,
 * and handles the submission of these details along with associated vaccination and user information into a database.
 *
 * This activity extends [AppCompatActivity] and implements [View.OnClickListener] for handling click events on UI elements.
 */
class ScheduleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnSelectDate: Button
    private lateinit var btnSelectTime: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedTime: TextView
    private lateinit var tvNextDoseDate: TextView
    private lateinit var calendarView: CalendarView

    private var selectedDateInMillis: Long = 0
    private var selectedTime = "09:00"

    /**
     * Initializes the activity, sets the content view, and configures UI components. It sets click listeners
     * on the date and time selection buttons and handles the logic for displaying and hiding the calendar view.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        btnSelectDate = findViewById(R.id.btnSelectDate)
        btnSelectTime = findViewById(R.id.btnSelectTime)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvSelectedTime = findViewById(R.id.tvSelectedTime)
        tvNextDoseDate = findViewById(R.id.tvNextDoseDate)
        calendarView = findViewById(R.id.calendarView)

        tvSelectedTime.text = "Selected Time: $selectedTime"

        btnSelectDate.setOnClickListener(this)
        btnSelectTime.setOnClickListener(this)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            selectedDateInMillis = selectedDate.timeInMillis
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            tvSelectedDate.text = "Selected Date: ${dateFormat.format(selectedDate.time)}"
            displayNextDoseDate()
        }
    }

    /**
     * Manages click events for the UI elements. Depending on which element is clicked, a date picker dialog or
     * a time picker dialog is shown.
     *
     * @param view The view that was clicked.
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSelectDate -> {
                if (calendarView.visibility == View.GONE) {
                    calendarView.visibility = View.VISIBLE
                } else {
                    calendarView.visibility = View.GONE
                }
            }
            R.id.btnSelectTime -> {
                showTimePickerDialog()
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val defaultHour = selectedTime.split(":")[0].toInt()  // Extract hour from default time
        val defaultMinute = selectedTime.split(":")[1].toInt()  // Extract minute from default time

        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            tvSelectedTime.text = "Selected Time: $selectedTime"
            insertDateIntoDatabase(vaccineId = 101, userId = 501, status = "Scheduled", dose = 1, intervalBetweenDoses = 30)
        }, defaultHour, defaultMinute, true).show()
    }


    private fun displayNextDoseDate() {
        val interval = 30
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateInMillis
        calendar.add(Calendar.DAY_OF_YEAR, interval)

        val nextDoseDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        tvNextDoseDate.text = "Next Dose: $nextDoseDate"
    }

    private fun insertDateIntoDatabase(vaccineId: Int?, userId: Int?, status: String?, dose: Int?, intervalBetweenDoses: Int?) {
        lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val conn: Connection = DBconnection.getConnection()
                val randomId = Random.nextInt(1000000)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val formattedDateTime = dateFormat.format(Date(selectedDateInMillis)) + " " + selectedTime
                val sql = "INSERT INTO Schedule_table (id, scheduledDate, vaccineId, userId, status, dose, intervalBetweenDoses) VALUES (?, ?, ?, ?, ?, ?, ?)"
                val statement = conn.prepareStatement(sql)
                statement.setInt(1, randomId)
                statement.setString(2, formattedDateTime)
                statement.setObject(3, vaccineId)
                statement.setObject(4, userId)
                statement.setString(5, status)
                statement.setObject(6, dose)
                statement.setObject(7, intervalBetweenDoses)
                val result = statement.executeUpdate()
                withContext(Dispatchers.Main) {
                    if (result > 0) {
                        Toast.makeText(applicationContext, "Data saved successfully with ID: $randomId", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Failed to save the data.", Toast.LENGTH_SHORT).show()
                    }
                }
                conn.close()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
            }
        }
    }

    /**
     * Inserts a scheduled date into the database based on user input and selected date and time.
     *
     * @param vaccineId Optional ID of the vaccine.
     * @param userId Optional ID of the user.
     * @param status Optional status of the vaccination.
     * @param dose Optional dose number of the vaccination.
     * @param intervalBetweenDoses Optional interval between doses.
     */
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        lifecycleScope.launch {
            Toast.makeText(applicationContext, "Error: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
        }
        exception.printStackTrace()
    }
}
