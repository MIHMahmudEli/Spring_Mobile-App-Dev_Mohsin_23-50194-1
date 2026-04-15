package com.example.universityeventapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityeventapp.R
import com.example.universityeventapp.model.Event

class EventAdapter(
    private var events: List<Event>,
    private val onItemClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBanner: ImageView = view.findViewById(R.id.ivEventBanner)
        val tvTitle: TextView = view.findViewById(R.id.tvEventTitle)
        val tvCategory: TextView = view.findViewById(R.id.tvEventCategory)
        val tvDate: TextView = view.findViewById(R.id.tvEventDate)
        val tvVenue: TextView = view.findViewById(R.id.tvEventVenue)
        val tvPrice: TextView = view.findViewById(R.id.tvEventPrice)
        val tvSeats: TextView = view.findViewById(R.id.tvAvailableSeats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvTitle.text = event.title
        holder.tvCategory.text = event.category
        holder.tvDate.text = "${event.date} • ${event.time}"
        holder.tvVenue.text = event.venue
        holder.tvPrice.text = if (event.price == 0.0) "Free" else "$${String.format("%.2f", event.price)}"
        holder.tvSeats.text = "${event.availableSeats} seats left"
        
        // Placeholder background for banner
        holder.ivBanner.setBackgroundColor(getColorForCategory(event.category))

        holder.itemView.setOnClickListener { onItemClick(event) }
    }

    override fun getItemCount() = events.size

    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

    private fun getColorForCategory(category: String): Int {
        return when (category) {
            "Tech" -> 0xFFE1F5FE.toInt()
            "Sports" -> 0xFFF1F8E9.toInt()
            "Cultural" -> 0xFFFFF3E0.toInt()
            "Academic" -> 0xFFF3E5F5.toInt()
            else -> 0xFFEEEEEE.toInt()
        }
    }
}
