package com.example.shopsmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopsmart.ProductClickListner
import com.example.shopsmart.R
import com.example.shopsmart.databinding.HorizontalProductItemBinding
import com.example.shopsmart.databinding.ProductItemBinding
import com.example.shopsmart.modelClass.ProductModel

class HorizontalProductAdapter(private var products: List<ProductModel>, private var listener: ProductClickListner) : RecyclerView.Adapter<HorizontalProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: HorizontalProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductModel) {
            binding.productName.text = product.name
            binding.price.text = "₹ ${product.price}"
            binding.rating.text = product.rating.toString()
            Glide.with(binding.productImage.context).load(product.imageUrl).into(binding.productImage)

           // binding.productImage.setImageResource(R.drawable.item_1)

            // Set the favorite icon based on the favorite status
            binding.favourite.setImageResource(
                if (product.favoriteProduct) R.drawable.red_hearts else R.drawable.blank_heart_fev_icon
            )

            binding.favourite.setOnClickListener {
                // Toggle the favorite status
                product.favoriteProduct = !product.favoriteProduct
                listener.onFavouriteClick(product)
                notifyItemChanged(adapterPosition)
            }

            binding.root.setOnClickListener {
                listener.onClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = HorizontalProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateData(newProducts: List<ProductModel>) {
        this.products = newProducts
        notifyDataSetChanged()
    }
}
