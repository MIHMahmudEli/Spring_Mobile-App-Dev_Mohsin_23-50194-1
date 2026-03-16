package com.example.contactbookapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvEmptyState: TextView
    private lateinit var adapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize Views
        listView = findViewById(R.id.listView)
        searchView = findViewById(R.id.searchView)
        fabAdd = findViewById(R.id.fabAdd)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        // Setup Adapter
        adapter = ContactAdapter(this, contactList)
        listView.adapter = adapter

        // FAB Click: Add Contact
        fabAdd.setOnClickListener {
            showAddContactDialog()
        }

        // ListView Item Click: Show Toast
        listView.setOnItemClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            if (contact != null) {
                Toast.makeText(
                    this,
                    "Name: ${contact.name}\nPhone: ${contact.phone}\nEmail: ${contact.email}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // ListView Item Long Press: Delete Contact
        listView.setOnItemLongClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            if (contact != null) {
                showDeleteConfirmationDialog(contact)
            }
            true
        }

        // SearchView: Filter Contacts
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText) {
                    updateEmptyState()
                }
                return true
            }
        })

        // Add some dummy data initially
        addDummyData()
    }

    private fun addDummyData() {
        contactList.add(Contact("Alif F AIUB B4", "01820154098", "alif@example.com"))
        contactList.add(Contact("Jam F AIUB B4", "0170154087", "jam@example.com"))
        contactList.add(Contact("Nihan F AIUB B4", "01944228011", "nihan@example.com"))
        adapter.updateData(contactList)
        updateEmptyState()
    }

    private fun showAddContactDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPhone = dialogView.findViewById<EditText>(R.id.etPhone)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        AlertDialog.Builder(this)
            .setTitle("Add New Contact")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                val email = etEmail.text.toString().trim()

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    val newContact = Contact(name, phone, email)
                    contactList.add(newContact)
                    adapter.updateData(contactList)
                    updateEmptyState()
                    Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Name and Phone are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle("Delete Contact")
            .setMessage("Are you sure you want to delete ${contact.name}?")
            .setPositiveButton("Delete") { _, _ ->
                contactList.remove(contact)
                adapter.updateData(contactList)
                updateEmptyState()
                Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateEmptyState() {
        if (adapter.count == 0) {
            tvEmptyState.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
    }

}