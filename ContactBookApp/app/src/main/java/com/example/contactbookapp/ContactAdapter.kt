package com.example.contactbookapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView

class ContactAdapter(context: Context, private var contacts: MutableList<Contact>) :
    ArrayAdapter<Contact>(context, R.layout.item_contact, contacts) {

    private var allContacts: List<Contact> = contacts.toList()
    private var filteredContacts: MutableList<Contact> = contacts

    override fun getCount(): Int = filteredContacts.size
    override fun getItem(position: Int): Contact? = filteredContacts[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.tvAvatar),
                view.findViewById(R.id.tvName),
                view.findViewById(R.id.tvPhone),
                view.findViewById(R.id.ivCall)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val contact = getItem(position)
        if (contact != null) {
            holder.name.text = contact.name
            holder.phone.text = contact.phone
            holder.avatar.text = contact.initial

            // Set dynamic background color for avatar
            val color = getColorForInitial(contact.initial)
            val drawable = holder.avatar.background as GradientDrawable
            drawable.setColor(color)
        }

        return view
    }

    private fun getColorForInitial(initial: String): Int {
        val colors = listOf(
            "#F44336", "#E91E63", "#9C27B0", "#673AB7",
            "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
            "#009688", "#4CAF50", "#8BC34A", "#CDDC39",
            "#FFEB3B", "#FFC107", "#FF9800", "#FF5722"
        )
        if (initial.isEmpty()) return Color.GRAY
        val index = initial[0].uppercaseChar().code % colors.size
        return Color.parseColor(colors[index])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.lowercase() ?: ""
                val filterResults = FilterResults()

                filterResults.values = if (queryString.isEmpty()) {
                    allContacts
                } else {
                    allContacts.filter {
                        it.name.lowercase().contains(queryString)
                    }
                }
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredContacts = (results?.values as List<Contact>).toMutableList()
                notifyDataSetChanged()
            }
        }
    }

    fun updateData(newContacts: List<Contact>) {
        allContacts = newContacts.toList()
        filteredContacts = newContacts.toMutableList()
        notifyDataSetChanged()
    }

    private class ViewHolder(
        val avatar: TextView,
        val name: TextView,
        val phone: TextView,
        val callIcon: ImageView
    )
}
