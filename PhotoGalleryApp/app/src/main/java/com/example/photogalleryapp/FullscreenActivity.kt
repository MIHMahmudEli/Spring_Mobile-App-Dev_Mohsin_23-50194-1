package com.example.photogalleryapp

import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageButton
import android.widget.ImageView
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FullscreenActivity : AppCompatActivity() {

    private lateinit var imgView: ImageView
    private lateinit var matrix: Matrix
    private var scale = 1f
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.TRANSPARENT
            )
        )
        setContentView(R.layout.activity_fullscreen)

        imgView = findViewById(R.id.imgViewFullscreen)
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        
        val resourceId = intent.getIntExtra("imageResourceId", 0)
        if (resourceId != 0) {
            imgView.setImageResource(resourceId)
        }

        matrix = Matrix()
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        btnBack.setOnClickListener {
            finish()
        }

        // Apply insets to the root view to avoid overlapping with system bars if needed,
        // but for full screen images, we often want the image behind the status bar.
        // We will only pad the back button.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnBack)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = v.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = systemBars.top + 16.dpToPx()
            params.leftMargin = systemBars.left + 16.dpToPx()
            v.layoutParams = params
            insets
        }

        // Center the image initially
        imgView.post {
            val drawable = imgView.drawable ?: return@post
            val drawableWidth = drawable.intrinsicWidth.toFloat()
            val drawableHeight = drawable.intrinsicHeight.toFloat()
            val viewWidth = imgView.width.toFloat()
            val viewHeight = imgView.height.toFloat()

            val scaleX = viewWidth / drawableWidth
            val scaleY = viewHeight / drawableHeight
            val initialScale = Math.min(scaleX, scaleY)

            matrix.setScale(initialScale, initialScale)
            matrix.postTranslate((viewWidth - drawableWidth * initialScale) / 2f,
                                 (viewHeight - drawableHeight * initialScale) / 2f)
            imgView.imageMatrix = matrix
            scale = initialScale
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event!!)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val focusX = detector.focusX
            val focusY = detector.focusY
            val scaleFactor = detector.scaleFactor
            
            val newScale = scale * scaleFactor
            if (newScale in 0.5f..10f) {
                scale = newScale
                matrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
                imgView.imageMatrix = matrix
            }
            return true
        }
    }
}
