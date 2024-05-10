package com.example.vaccination_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * An activity that serves as the main menu for the application. This activity displays various options to
 * navigate to other activities such as adding records, viewing history, managing schedules, and viewing a custom calendar.
 * Each button in the menu corresponds to a different feature of the application, and clicking a button will navigate
 * to the respective activity.
 *
 * Implements [View.OnClickListener] to handle button click events.
 */
class MenuActivity : AppCompatActivity(), View.OnClickListener {

    /**
     * Initializes the activity, sets the content view, and sets up click listeners for each menu item.
     * Each menu item, when clicked, navigates to a different part of the application.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Initialize click listeners for each button in the menu
        findViewById<View>(R.id.btnAddRecord).setOnClickListener(this)
        findViewById<View>(R.id.btnViewHistory).setOnClickListener(this)
        findViewById<View>(R.id.btnSchedule).setOnClickListener(this)
        findViewById<View>(R.id.btnViewCalendar).setOnClickListener(this)
    }

    /**
     * Handles click events on the UI components. This method uses a when statement to determine which view
     * was clicked and navigates to the appropriate activity using intents.
     *
     * @param view The view that was clicked.
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  .btnAddRecord -> {
                startActivity(Intent(this, AddVaccinationRecordActivity::class.java))
            }
            R.id.btnViewHistory -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
            R.id.btnSchedule -> {
                startActivity(Intent(this, ScheduleActivity::class.java))
            }
//            R.id.btnViewCalendar -> {
//                startActivity(Intent(this, CustomCalendarActivity::class.java))
//            }
        }
    }
}
