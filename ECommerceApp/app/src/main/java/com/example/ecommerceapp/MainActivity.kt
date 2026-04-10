package com.example.ecommerceapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var skeletonLayout: View
    private lateinit var emptyStateLayout: View
    private lateinit var chipGroup: ChipGroup
    private var mainMenu: Menu? = null
    
    private var allProducts = mutableListOf<Product>()
    private var displayedProducts = mutableListOf<Product>()
    private var cartItemsCount = 0
    private var isGridView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        skeletonLayout = findViewById(R.id.layoutSkeleton)
        emptyStateLayout = findViewById(R.id.layoutEmptyState)
        chipGroup = findViewById(R.id.chipGroupCategories)

        setupData()
        setupRecyclerView()
        setupFilters()
        setupItemTouchHelper()
        
        simulateLoading()
    }

    private fun setupData() {
        CartManager.initData()
        allProducts = CartManager.allProducts
        displayedProducts.addAll(allProducts)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(displayedProducts) { product ->
            product.inCart = true
            updateCartBadge()
            productAdapter.notifyDataSetChanged()
            Snackbar.make(recyclerView, "${product.name} added to cart", Snackbar.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productAdapter
    }

    private fun setupFilters() {
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val category = when (checkedId) {
                R.id.chipElectronics -> "Electronics"
                R.id.chipClothing -> "Clothing"
                R.id.chipBooks -> "Books"
                R.id.chipFood -> "Food"
                R.id.chipToys -> "Toys"
                else -> "All"
            }
            filterProducts(category)
        }
    }

    private fun filterProducts(category: String, query: String = "") {
        val filtered = allProducts.filter {
            (category == "All" || it.category == category) &&
            (query.isEmpty() || it.name.contains(query, ignoreCase = true))
        }
        displayedProducts.clear()
        displayedProducts.addAll(filtered)
        productAdapter.notifyDataSetChanged()
        
        emptyStateLayout.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupItemTouchHelper() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                productAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedProduct = productAdapter.onItemDismiss(position)
                
                Snackbar.make(recyclerView, "Product deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        displayedProducts.add(position, removedProduct)
                        productAdapter.notifyItemInserted(position)
                    }.show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    private fun simulateLoading() {
        recyclerView.visibility = View.GONE
        skeletonLayout.visibility = View.VISIBLE
        
        Handler(Looper.getMainLooper()).postDelayed({
            skeletonLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        mainMenu = menu
        menuInflater.inflate(R.menu.main_menu, menu)
        
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val selectedChipId = chipGroup.checkedChipId
                val category = when (selectedChipId) {
                    R.id.chipElectronics -> "Electronics"
                    R.id.chipClothing -> "Clothing"
                    R.id.chipBooks -> "Books"
                    R.id.chipFood -> "Food"
                    R.id.chipToys -> "Toys"
                    else -> "All"
                }
                filterProducts(category, newText ?: "")
                return true
            }
        })

        updateCartBadge()
        return true
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
        productAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_view -> {
                isGridView = !isGridView
                item.setIcon(if (isGridView) R.drawable.ic_list else R.drawable.ic_grid)
                recyclerView.layoutManager = if (isGridView) GridLayoutManager(this, 2) else LinearLayoutManager(this)
                productAdapter.isGridView = isGridView
                true
            }
            R.id.action_cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateCartBadge() {
        val cartItem = mainMenu?.findItem(R.id.action_cart) ?: return
        val cartView = cartItem.actionView
        val badge = cartView?.findViewById<TextView>(R.id.cart_badge)
        val count = CartManager.getCartCount()
        
        cartView?.setOnClickListener {
            onOptionsItemSelected(cartItem)
        }

        if (count > 0) {
            badge?.text = count.toString()
            badge?.visibility = View.VISIBLE
        } else {
            badge?.visibility = View.GONE
        }
    }

    
    // Static list to share cart state between activities for simplicity in this lab
    companion object {
        fun getCartItems(allProducts: List<Product>) = allProducts.filter { it.inCart }
    }
    
    // To make sure CartActivity can see the same objects, I'll use a hacky singleton or object.
    // In a real app, use a ViewModel/Repository.
}