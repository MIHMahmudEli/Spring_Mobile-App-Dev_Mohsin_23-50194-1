package com.example.universityeventapp.model

import java.io.Serializable

data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val time: String,
    val venue: String,
    val category: String,
    val description: String,
    val price: Double,
    val totalSeats: Int,
    var availableSeats: Int,
    val imageRes: Int,
    val speakerName: String = "Dr. John Smith",
    val speakerDesignation: String = "Senior Professor",
    val speakerImageRes: Int = 0
) : Serializable
