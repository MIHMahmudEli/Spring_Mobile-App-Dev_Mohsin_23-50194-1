package com.example.ecommerceapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: ProductAdapter
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: MaterialButton
    private var cartItems = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarCart)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerView = findViewById(R.id.recyclerViewCart)
        tvTotal = findViewById(R.id.tvTotalPrice)
        btnCheckout = findViewById(R.id.btnCheckout)

        setupCart()
        updateTotal()
        
        btnCheckout.setOnClickListener {
            if (cartItems.isEmpty()) {
                Snackbar.make(recyclerView, "Cart is empty", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(recyclerView, "Order placed successfully!", Snackbar.LENGTH_LONG).show()
                // Clear cart
                CartManager.allProducts.forEach { it.inCart = false }
                finish()
            }
        }
    }

    private fun setupCart() {
        cartItems = CartManager.getCartItems().toMutableList()
        cartAdapter = ProductAdapter(cartItems) { /* No-op in cart */ }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cartAdapter

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val product = cartItems[position]
                product.inCart = false
                cartItems.removeAt(position)
                cartAdapter.notifyItemRemoved(position)
                updateTotal()
                Snackbar.make(recyclerView, "${product.name} removed", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        product.inCart = true
                        cartItems.add(position, product)
                        cartAdapter.notifyItemInserted(position)
                        updateTotal()
                    }.show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    private fun updateTotal() {
        val total = CartManager.getTotalPrice()
        tvTotal.text = "$${String.format("%.2f", total)}"
    }
}
