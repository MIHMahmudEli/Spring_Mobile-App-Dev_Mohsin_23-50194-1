package com.example.photogalleryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var gvPhotos: GridView
    private lateinit var adapter: PhotoAdapter
    private val allPhotos = mutableListOf<Photo>()
    private val displayedPhotos = mutableListOf<Photo>()

    private lateinit var selectionToolbar: LinearLayout
    private lateinit var txtSelectedCount: TextView
    private lateinit var btnDelete: ImageButton
    private lateinit var btnShare: ImageButton
    private lateinit var fabAdd: FloatingActionButton

    private var currentCategory = "All"

    private val backPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (adapter.getSelectionMode()) {
                exitSelectionMode()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        loadInitialData()
        setupListeners()
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun setupUI() {
        gvPhotos = findViewById(R.id.photoGridView)
        selectionToolbar = findViewById(R.id.selectionToolbar)
        txtSelectedCount = findViewById(R.id.txtSelectedCount)
        btnDelete = findViewById(R.id.btnDelete)
        btnShare = findViewById(R.id.btnShare)
        fabAdd = findViewById(R.id.fabAdd)

        adapter = PhotoAdapter(this, displayedPhotos)
        gvPhotos.adapter = adapter
    }

    private fun loadInitialData() {
        allPhotos.add(Photo(1, R.drawable.nature1, "Nature 1", "Nature"))
        allPhotos.add(Photo(2, R.drawable.nature2, "Nature 2", "Nature"))
        allPhotos.add(Photo(3, R.drawable.nature3, "Nature 3", "Nature"))
        allPhotos.add(Photo(4, R.drawable.city1, "City 1", "City"))
        allPhotos.add(Photo(5, R.drawable.city2, "City 2", "City"))
        allPhotos.add(Photo(6, R.drawable.city3, "City 3", "City"))
        allPhotos.add(Photo(7, R.drawable.animal1, "Animal 1", "Animals"))
        allPhotos.add(Photo(8, R.drawable.animal2, "Animal 2", "Animals"))
        allPhotos.add(Photo(9, R.drawable.food1, "Food 1", "Food"))
        allPhotos.add(Photo(10, R.drawable.food2, "Food 2", "Food"))
        allPhotos.add(Photo(11, R.drawable.travel1, "Travel 1", "Travel"))
        allPhotos.add(Photo(12, R.drawable.travel2, "Travel 2", "Travel"))

        filterByCategory("All")
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.tabAll).setOnClickListener { filterByCategory("All") }
        findViewById<Button>(R.id.tabNature).setOnClickListener { filterByCategory("Nature") }
        findViewById<Button>(R.id.tabCity).setOnClickListener { filterByCategory("City") }
        findViewById<Button>(R.id.tabAnimals).setOnClickListener { filterByCategory("Animals") }
        findViewById<Button>(R.id.tabFood).setOnClickListener { filterByCategory("Food") }
        findViewById<Button>(R.id.tabTravel).setOnClickListener { filterByCategory("Travel") }

        gvPhotos.setOnItemClickListener { _, _, position, _ ->
            val photo = displayedPhotos[position]
            if (adapter.getSelectionMode()) {
                toggleSelection(photo)
            } else {
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putExtra("imageResourceId", photo.resourceId)
                startActivity(intent)
            }
        }

        gvPhotos.setOnItemLongClickListener { _, _, position, _ ->
            if (!adapter.getSelectionMode()) {
                adapter.setSelectionMode(true)
                backPressedCallback.isEnabled = true
                selectionToolbar.visibility = View.VISIBLE
                val photo = displayedPhotos[position]
                toggleSelection(photo)
            }
            true
        }

        btnDelete.setOnClickListener {
            val selectedPhotos = displayedPhotos.filter { it.isSelected }
            val count = selectedPhotos.size
            allPhotos.removeAll(selectedPhotos)
            filterByCategory(currentCategory)

            Toast.makeText(this, "$count photos deleted", Toast.LENGTH_SHORT).show()
            exitSelectionMode()
        }

        btnShare.setOnClickListener {
            val selectedCount = displayedPhotos.count { it.isSelected }
            Toast.makeText(this, "Sharing $selectedCount photos", Toast.LENGTH_SHORT).show()
            exitSelectionMode()
        }

        fabAdd.setOnClickListener {
            val randomId = Random().nextInt(1000)
            val resourceIds = listOf(
                R.drawable.nature1, R.drawable.nature2, R.drawable.city1,
                R.drawable.city2, R.drawable.animal1, R.drawable.animal2
            )
            val randomRes = resourceIds[Random().nextInt(resourceIds.size)]
            val newPhoto = Photo(randomId, randomRes, "New Photo $randomId", currentCategory)
            allPhotos.add(newPhoto)
            filterByCategory(currentCategory)
            Toast.makeText(this, "New $currentCategory photo added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterByCategory(category: String) {
        currentCategory = category
        displayedPhotos.clear()
        if (category == "All") {
            displayedPhotos.addAll(allPhotos)
        } else {
            displayedPhotos.addAll(allPhotos.filter { it.category == category })
        }
        adapter.updateData(displayedPhotos)
    }

    private fun toggleSelection(photo: Photo) {
        photo.isSelected = !photo.isSelected
        val selectedCount = displayedPhotos.count { it.isSelected }
        if (selectedCount == 0) {
            exitSelectionMode()
        } else {
            txtSelectedCount.text = "$selectedCount selected"
            adapter.notifyDataSetChanged()
        }
    }

    private fun exitSelectionMode() {
        adapter.setSelectionMode(false)
        backPressedCallback.isEnabled = false
        selectionToolbar.visibility = View.GONE
        allPhotos.forEach { it.isSelected = false }
        adapter.notifyDataSetChanged()
    }
}