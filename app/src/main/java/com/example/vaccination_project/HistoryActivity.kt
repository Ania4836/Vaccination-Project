package com.example.vaccination_project

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

class HistoryActivity : AppCompatActivity() {

    private lateinit var scheduleListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        scheduleListView = findViewById(R.id.scheduleListView)

        fetchAndDisplaySchedules()
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
                        val id = resultSet.getInt("id")
                        val vaccineId = resultSet.getInt("vaccineId")
                        val doctorId = resultSet.getInt("doctorId")
                        val scheduledTime = resultSet.getTime("scheduledTime")
                        val status = resultSet.getString("status")
                        val scheduledDate = resultSet.getDate("scheduledDate")
                        val dose = resultSet.getInt("dose")
                        val intervalBetweenDoses = resultSet.getInt("intervalBetweenDoses")

                        val scheduleItem = ScheduleDate(
                            id,
                            vaccineId,
                            userId,
                            doctorId,
                            scheduledTime,
                            status,
                            scheduledDate,
                            dose,
                            intervalBetweenDoses
                        )
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

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
