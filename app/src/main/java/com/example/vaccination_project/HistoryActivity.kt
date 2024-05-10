package com.example.vaccination_project

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccination_project.UpdateScheduleActivity
import com.example.vaccination_project.db_connection.DBconnection
import com.example.vaccination_project.db_connection.schedule_date.ScheduleDate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.ResultSet

class HistoryActivity : AppCompatActivity() {

    private lateinit var scheduleListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        scheduleListView = findViewById(R.id.scheduleListView)

        fetchAndDisplaySchedules()

        // Set click listener for list view items
        scheduleListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as ScheduleDate
            updateSchedule(selectedItem)
        }
    }

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

    private fun updateSchedule(selectedItem: ScheduleDate) {
        // You can implement the logic to update the schedule here
        // For example, you can start an activity to edit the schedule details
        val intent = Intent(this, UpdateScheduleActivity::class.java)
        intent.putExtra("scheduleId", selectedItem.id)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
