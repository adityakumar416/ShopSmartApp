package com.example.shopsmart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopsmart.adapter.HorizontalProductAdapter
import com.example.shopsmart.modelClass.ProductModel

class MixedAdapter(
    private var favoriteProducts: List<ProductModel>,
    private var allProducts: List<ProductModel>,
    private val clickListener: ProductClickListner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_GRID_ITEM = 0
        const val VIEW_TYPE_HORIZONTAL_LIST = 1
    }

    fun updateData(favorites: List<ProductModel>, all: List<ProductModel>) {
        favoriteProducts = favorites
        allProducts = all
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        // Horizontal list appears after every 2 grid items
        return if ((position + 1) % 3 == 0) {
            VIEW_TYPE_HORIZONTAL_LIST
        } else {
            VIEW_TYPE_GRID_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_item, parent, false)
                GridViewHolder(view)
            }

            VIEW_TYPE_HORIZONTAL_LIST -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_horizontal_list, parent, false)
                HorizontalViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        // Number of grid items + number of horizontal lists
        val gridItemCount = favoriteProducts.size
        val horizontalItemCount = gridItemCount / 2 // Horizontal list after every 2 grid items
        return gridItemCount + horizontalItemCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GridViewHolder -> {
                val actualPosition = position - (position / 3)
                if (actualPosition < favoriteProducts.size) {
                    val product = favoriteProducts[actualPosition]
                    holder.bind(product, clickListener)
                }
            }

            is HorizontalViewHolder -> {
                holder.bind(allProducts, clickListener)
            }
        }
    }

    // ViewHolder for Grid Items
    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: ProductModel, clickListener: ProductClickListner) {
            val productImage = itemView.findViewById<ImageView>(R.id.product_image)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.price)
            val favoriteIcon = itemView.findViewById<ImageView>(R.id.favourite)


            Glide.with(productImage.context).load(product.imageUrl).into(productImage)

            // Set product details
            productName.text = product.name
            productPrice.text = "₹ ${product.price}"

            // Set click listeners
            itemView.setOnClickListener {
                clickListener.onClick(product)
            }

            // Set the favorite icon based on the favorite status
            favoriteIcon.setImageResource(
                if (product.favoriteProduct) R.drawable.red_hearts else R.drawable.blank_heart_fev_icon
            )

            favoriteIcon.setOnClickListener {
                // Toggle the favorite status
                product.favoriteProduct = !product.favoriteProduct
                clickListener.onFavouriteClick(product)
                notifyItemChanged(adapterPosition)
            }
        }
    }

    // ViewHolder for Horizontal RecyclerView
    inner class HorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val horizontalRecyclerView: RecyclerView =
            itemView.findViewById(R.id.horizontalRecyclerView)

        fun bind(products: List<ProductModel>, clickListener: ProductClickListner) {
            horizontalRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            horizontalRecyclerView.adapter = HorizontalProductAdapter(products, clickListener)
        }
    }
}
