package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.universityeventapp.adapter.EventAdapter
import com.example.universityeventapp.model.Event
import com.google.android.material.chip.ChipGroup

class EventListActivity : AppCompatActivity() {

    private lateinit var adapter: EventAdapter
    private var allEvents = mutableListOf<Event>()
    private var currentQuery: String? = null
    private var currentCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        setupData()
        setupRecyclerView()
        setupSearchView()
        setupFilterChips()
    }

    private fun setupData() {
        allEvents = mutableListOf(
            Event(1, "Annual Tech Symposium", "OCT 24, 2026", "10:00 AM", "Main Auditorium", "Tech", "Deep dive into future tech.", 25.0, 100, 45, 0),
            Event(2, "Inter-University Cricket", "NOV 05, 2026", "09:00 AM", "Sports Complex", "Sports", "Exciting cricket finals.", 0.0, 500, 120, 0),
            Event(3, "Cultural Night 2026", "DEC 12, 2026", "06:00 PM", "Open Air Theater", "Cultural", "Music, Dance and more.", 15.0, 300, 50, 0),
            Event(4, "AI & Robotics Workshop", "OCT 30, 2026", "02:00 PM", "Lab Block B", "Tech", "Hands-on AI training.", 50.0, 30, 5, 0),
            Event(5, "Guest Lecture: Economy", "NOV 10, 2026", "11:00 AM", "Seminar Hall 1", "Academic", "Insight into global markets.", 0.0, 150, 80, 0),
            Event(6, "Marathon For Charity", "OCT 15, 2026", "06:00 AM", "University Gate", "Sports", "Run for a cause.", 10.0, 1000, 400, 0),
            Event(7, "Art & Craft Exhibition", "NOV 20, 2026", "10:00 AM", "Art Gallery", "Social", "Display of student talent.", 5.0, 200, 100, 0),
            Event(8, "Coding Hackathon", "DEC 05, 2026", "09:00 AM", "Computer Center", "Tech", "48-hour coding challenge.", 20.0, 50, 12, 0)
        )
    }

    private fun setupRecyclerView() {
        val rvEvents = findViewById<RecyclerView>(R.id.recyclerViewEvents)
        rvEvents.layoutManager = LinearLayoutManager(this)
        adapter = EventAdapter(allEvents) { event ->
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("EVENT_DATA", event)
            startActivity(intent)
        }
        rvEvents.adapter = adapter
    }

    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText
                applyFilters()
                return true
            }
        })
    }

    private fun setupFilterChips() {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = if (checkedIds.isEmpty()) -1 else checkedIds[0]
            currentCategory = when (checkedId) {
                R.id.chipTech -> "Tech"
                R.id.chipSports -> "Sports"
                R.id.chipCultural -> "Cultural"
                R.id.chipAcademic -> "Academic"
                else -> null
            }
            applyFilters()
        }
    }

    private fun applyFilters() {
        var filteredList = allEvents
        if (!currentQuery.isNullOrEmpty()) {
            filteredList = filteredList.filter { it.title.contains(currentQuery!!, ignoreCase = true) }.toMutableList()
        }
        if (!currentCategory.isNullOrEmpty()) {
            filteredList = filteredList.filter { it.category == currentCategory }.toMutableList()
        }
        adapter.updateData(filteredList)
    }
}
