package com.example.ecommerceapp

object CartManager {
    val allProducts = mutableListOf<Product>()
    
    fun initData() {
        if (allProducts.isEmpty()) {
            allProducts.addAll(listOf(
                Product(1, "Smartphone Pro", 899.99, 4.8f, "Electronics", R.drawable.ic_launcher_foreground),
                Product(2, "Cotton T-Shirt", 19.99, 4.2f, "Clothing", R.drawable.ic_launcher_foreground),
                Product(3, "Android Development Guide", 45.00, 4.9f, "Books", R.drawable.ic_launcher_foreground),
                Product(4, "Organic Pizza", 12.50, 4.5f, "Food", R.drawable.ic_launcher_foreground),
                Product(5, "Action Figure", 25.00, 4.0f, "Toys", R.drawable.ic_launcher_foreground),
                Product(6, "Laptop Ultra", 1200.00, 4.7f, "Electronics", R.drawable.ic_launcher_foreground),
                Product(7, "Blue Jeans", 39.99, 4.3f, "Clothing", R.drawable.ic_launcher_foreground)
            ))
        }
    }

    fun getCartItems() = allProducts.filter { it.inCart }
    
    fun getCartCount() = allProducts.count { it.inCart }
    
    fun getTotalPrice() = allProducts.filter { it.inCart }.sumOf { it.price }
}
