package com.example.newsreaderapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val scrollView = findViewById<NestedScrollView>(R.id.scrollView)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)
        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        val fabBackToTop = findViewById<FloatingActionButton>(R.id.fabBackToTop)

        // Navigation Buttons
        val navIntro = findViewById<Button>(R.id.navIntro)
        val navKeyPoints = findViewById<Button>(R.id.navKeyPoints)
        val navAnalysis = findViewById<Button>(R.id.navAnalysis)
        val navConclusion = findViewById<Button>(R.id.navConclusion)

        // Sections
        val sectionIntro = findViewById<View>(R.id.sectionIntro)
        val sectionKeyPoints = findViewById<View>(R.id.sectionKeyPoints)
        val sectionAnalysis = findViewById<View>(R.id.sectionAnalysis)
        val sectionConclusion = findViewById<View>(R.id.sectionConclusion)

        // Event Listeners
        navIntro.setOnClickListener { scrollToView(scrollView, sectionIntro) }
        navKeyPoints.setOnClickListener { scrollToView(scrollView, sectionKeyPoints) }
        navAnalysis.setOnClickListener { scrollToView(scrollView, sectionAnalysis) }
        navConclusion.setOnClickListener { scrollToView(scrollView, sectionConclusion) }

        fabBackToTop.setOnClickListener {
            scrollView.smoothScrollTo(0, 0)
        }

        btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked
            updateBookmarkIcon(btnBookmark)
            val message = if (isBookmarked) R.string.toast_bookmarked else R.string.toast_bookmark_removed
            Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
        }

        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "${getString(R.string.article_title)}\n\nRead more at The Daily Reader.")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Article"))
        }
    }
    private fun updateBookmarkIcon(button: ImageButton) {
        val iconRes = if (isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_border
        button.setImageResource(iconRes)
    }

    private fun scrollToView(scrollView: NestedScrollView, view: View) {
        var top = 0
        var current = view
        while (current.parent !== scrollView && current.parent != null) {
            top += current.top
            current = current.parent as View
        }
        scrollView.smoothScrollTo(0, top)
    }
}