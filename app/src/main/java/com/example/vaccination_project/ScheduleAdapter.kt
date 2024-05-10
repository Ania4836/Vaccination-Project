package com.example.vaccination_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.vaccination_project.db_connection.schedule_date.ScheduleDate

/**
 * An adapter for displaying vaccination schedules in a ListView. This adapter extends ArrayAdapter to
 * provide custom views for each item representing a scheduled vaccination. It handles the inflation of layout,
 * setting up view holders for efficient view handling, and binding schedule data to the views.
 *
 * @param context The current context. Used to inflate the layout file.
 * @param resource The resource ID for a layout file containing a layout to use when instantiating views.
 * @param objects The objects to represent in the ListView. These are ScheduleDate objects containing detailed information about each schedule.
 */
class ScheduleAdapter(context: Context, resource: Int, objects: List<ScheduleDate>) :
    ArrayAdapter<ScheduleDate>(context, resource, objects) {

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * Positional parameters allow the Adapter to avoid creating a new view by reusing an existing one,
     * improving performance by recycling views that are no longer visible.
     *
     * @param position The position in the list of data that should be displayed in the list item view.
     * @param convertView The old view to reuse, if possible. If it is not possible to convert this view
     *                    to display the correct data, this method can create a new view.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val viewHolder: ViewHolder

        // Check if an existing view is being reused, otherwise inflate the view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_schedule, parent, false)
            viewHolder = ViewHolder()
            viewHolder.scheduleIdTextView = itemView.findViewById(R.id.scheduleIdTextView)
            viewHolder.vaccineIdTextView = itemView.findViewById(R.id.vaccineIdTextView)
            viewHolder.doctorIdTextView = itemView.findViewById(R.id.doctorIdTextView)
            viewHolder.scheduledTimeTextView = itemView.findViewById(R.id.scheduledTimeTextView)
            viewHolder.statusTextView = itemView.findViewById(R.id.statusTextView)
            viewHolder.scheduledDateTextView = itemView.findViewById(R.id.scheduledDateTextView)
            viewHolder.doseTextView = itemView.findViewById(R.id.doseTextView)
            viewHolder.intervalTextView = itemView.findViewById(R.id.intervalTextView)
            itemView.tag = viewHolder
        } else {
            viewHolder = itemView.tag as ViewHolder
        }

        val currentItem = getItem(position)

        viewHolder.scheduleIdTextView.text = "Schedule ID: ${currentItem?.id}"
        viewHolder.vaccineIdTextView.text = "Vaccine ID: ${currentItem?.vaccineId}"
        if (currentItem?.doctorId != null && currentItem.doctorId != 0) {
            viewHolder.doctorIdTextView.text = "Doctor ID: ${currentItem.doctorId}"
        } else {
            viewHolder.doctorIdTextView.visibility = View.GONE
        }
        if (currentItem?.scheduledTime != null) {
            viewHolder.scheduledTimeTextView.text = "Scheduled Time: ${currentItem.scheduledTime}"
        } else {
            viewHolder.scheduledTimeTextView.visibility = View.GONE
        }
        if (currentItem?.status != null) {
            viewHolder.statusTextView.text = "Status: ${currentItem.status}"
        } else {
            viewHolder.statusTextView.visibility = View.GONE
        }
        if (currentItem?.scheduledDate != null) {
            viewHolder.scheduledDateTextView.text = "Scheduled Date: ${currentItem.scheduledDate}"
        } else {
            viewHolder.scheduledDateTextView.visibility = View.GONE
        }
        if (currentItem?.dose != null && currentItem.dose != 0) {
            viewHolder.doseTextView.text = "Dose: ${currentItem.dose}"
        } else {
            viewHolder.doseTextView.visibility = View.GONE
        }
        if (currentItem?.intervalBetweenDoses != null && currentItem.intervalBetweenDoses != 0) {
            viewHolder.intervalTextView.text = "Interval: ${currentItem.intervalBetweenDoses}"
        } else {
            viewHolder.intervalTextView.visibility = View.GONE
        }

        return itemView!!
    }

    /**
     * A ViewHolder design pattern to hold views to avoid frequent calls to findViewById which is costly in terms of performance.
     */
    private class ViewHolder {
        lateinit var scheduleIdTextView: TextView
        lateinit var vaccineIdTextView: TextView
        lateinit var doctorIdTextView: TextView
        lateinit var scheduledTimeTextView: TextView
        lateinit var statusTextView: TextView
        lateinit var scheduledDateTextView: TextView
        lateinit var doseTextView: TextView
        lateinit var intervalTextView: TextView
    }
}
