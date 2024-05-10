package com.example.vaccination_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * An adapter for the RecyclerView that manages a list of days in a month. This adapter is responsible
 * for providing views that represent each day in the calendar view.
 *
 * Each item in the RecyclerView is represented as a single day, enabling the display of all days in the month in a grid-like format.
 *
 * @param daysOfMonth A list of strings where each string represents a day in the month.
 */
class CalendarAdapter(private val daysOfMonth: List<String>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    /**
     * Provides a reference to the views for each data item. Complex data items may need more than one view per item,
     * and you provide access to all the views for a data item in a view holder.
     *
     * @property itemView The view corresponding to each item of data in the RecyclerView.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDay: TextView = itemView.findViewById(R.id.textViewDay)
    }

    /**
     * Creates new views (invoked by the layout manager). This method inflates the XML layout for each item of the RecyclerView.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_day_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager). This method updates the content of the ViewHolder
     * to reflect the item at the given position in the data set.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position
     * in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewDay.text = daysOfMonth[position]
    }

    /**
     * Returns the size of the data set (invoked by the layout manager). This method provides the number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return daysOfMonth.size
    }
}
