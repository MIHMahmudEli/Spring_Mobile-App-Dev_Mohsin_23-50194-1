package com.example.ecommerceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class ProductAdapter(
    private var products: MutableList<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_LIST = 1
        const val VIEW_TYPE_GRID = 2
    }

    var isGridView: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) VIEW_TYPE_GRID else VIEW_TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LIST) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_list, parent, false)
            ListViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_grid, parent, false)
            GridViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]
        if (holder is ListViewHolder) {
            holder.bind(product)
        } else if (holder is GridViewHolder) {
            holder.bind(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newList: List<Product>) {
        val diffResult = DiffUtil.calculateDiff(ProductDiffCallback(products, newList))
        products.clear()
        products.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(products, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(products, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    fun onItemDismiss(position: Int): Product {
        val removedProduct = products.removeAt(position)
        notifyItemRemoved(position)
        return removedProduct
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        private val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val btnCart: Button = itemView.findViewById(R.id.btnAddToCart)

        fun bind(product: Product) {
            tvName.text = product.name
            tvCategory.text = product.category
            ratingBar.rating = product.rating
            tvPrice.text = "$${product.price}"
            imgProduct.setImageResource(product.imageRes)
            
            btnCart.text = if (product.inCart) "In Cart" else "Add to Cart"
            btnCart.isEnabled = !product.inCart
            btnCart.setOnClickListener { onAddToCart(product) }
        }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        private val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val btnCart: ImageButton = itemView.findViewById(R.id.btnAddToCart)

        fun bind(product: Product) {
            tvName.text = product.name
            tvPrice.text = "$${product.price}"
            imgProduct.setImageResource(product.imageRes)
            
            btnCart.setImageResource(if (product.inCart) R.drawable.ic_cart else R.drawable.ic_cart_plus)
            btnCart.isEnabled = !product.inCart
            btnCart.setOnClickListener { onAddToCart(product) }
        }
    }

    class ProductDiffCallback(
        private val oldList: List<Product>,
        private val newList: List<Product>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
