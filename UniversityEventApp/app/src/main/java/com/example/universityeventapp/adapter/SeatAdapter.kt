package com.example.universityeventapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.universityeventapp.R

class SeatAdapter(
    private val context: Context,
    private val seats: List<Int> // 0: Available, 1: Booked, 2: Selected
) : BaseAdapter() {

    override fun getCount(): Int = seats.size
    override fun getItem(position: Int): Any = seats[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false)
        val ivSeat = view.findViewById<ImageView>(R.id.ivSeat)
        
        val state = seats[position]
        when (state) {
            0 -> ivSeat.setBackgroundResource(R.drawable.seat_available)
            1 -> ivSeat.setBackgroundResource(R.drawable.seat_booked)
            2 -> ivSeat.setBackgroundResource(R.drawable.seat_selected)
        }

        return view
    }
}
