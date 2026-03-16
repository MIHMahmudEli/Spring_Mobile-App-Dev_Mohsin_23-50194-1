package com.example.contactbookapp

data class Contact(
    val name: String,
    val phone: String,
    val email: String,
    val initial: String = name.firstOrNull()?.uppercase() ?: ""
)
