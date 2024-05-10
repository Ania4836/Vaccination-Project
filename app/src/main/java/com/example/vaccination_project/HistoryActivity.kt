package com.example.vaccination_project

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccination_project.db_connection.DBconnection
import com.example.vaccination_project.db_connection.schedule_date.ScheduleDate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.ResultSet

/**
 * An activity that displays a historical view of vaccination schedules for the currently logged-in user.
 * This activity fetches data from a database and populates a ListView with the schedule details. It is designed
 * to give users an easy way to view their past and upcoming vaccination appointments.
 *
 * Inherits from [AppCompatActivity] and provides a user interface for displaying the list of scheduled vaccinations.
 */
class HistoryActivity : AppCompatActivity() {

    private lateinit var scheduleListView: ListView

    /**
     * Initializes the activity, sets up the content view, and binds the ListView component. It also initiates
     * the process to fetch and display the schedules from the database.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        scheduleListView = findViewById(R.id.scheduleListView)

        fetchAndDisplaySchedules()

        scheduleListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as ScheduleDate
            updateSchedule(selectedItem)
        }
    }

    /**
     * Fetches schedule data for the currently authenticated user from the database and displays it in the ListView.
     * If no user is logged in, displays a toast message indicating that the user is not logged in.
     */
    private fun fetchAndDisplaySchedules() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val conn = DBconnection.getConnection()
                    val statement = conn.prepareStatement("SELECT * FROM Schedule_table WHERE userId = ?")
                    statement.setString(1, userId)
                    val resultSet: ResultSet = statement.executeQuery()

                    val scheduleList = mutableListOf<ScheduleDate>()
                    while (resultSet.next()) {
                        // Parse schedule data from ResultSet and add to scheduleList
                        val scheduleItem = parseScheduleItem(resultSet)
                        scheduleList.add(scheduleItem)
                    }

                    conn.close()

                    runOnUiThread {
                        val adapter = ScheduleAdapter(this@HistoryActivity, R.layout.list_item_schedule, scheduleList)
                        scheduleListView.adapter = adapter
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast("Error occurred while fetching schedules")
                }
            }
        } else {
            showToast("User not logged in")
        }
    }

    /**
     * Parses the ResultSet obtained from the database query and creates a ScheduleDate object.
     *
     * @param resultSet The ResultSet containing the schedule data.
     * @return A ScheduleDate object representing the parsed schedule data.
     */
    private fun parseScheduleItem(resultSet: ResultSet): ScheduleDate {
        // Parse ResultSet and create a ScheduleDate object
        return ScheduleDate(
            resultSet.getInt("id"),
            resultSet.getInt("vaccineId"),
            resultSet.getString("userId"),
            resultSet.getInt("doctorId"),
            resultSet.getTime("scheduledTime"),
            resultSet.getString("status"),
            resultSet.getDate("scheduledDate"),
            resultSet.getInt("dose"),
            resultSet.getInt("intervalBetweenDoses")
        )
    }

    /**
     * Opens the UpdateScheduleActivity for editing the selected schedule item.
     *
     * @param selectedItem The selected schedule item to be updated.
     */
    private fun updateSchedule(selectedItem: ScheduleDate) {
        val intent = Intent(this, UpdateScheduleActivity::class.java)
        intent.putExtra("scheduleId", selectedItem.id)
        startActivity(intent)
    }

    /**
     * Displays a Toast message on the UI thread.
     *
     * @param message The message to be displayed.
     */
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}