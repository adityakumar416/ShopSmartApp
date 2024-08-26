package com.example.shopsmart.viewAllProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopsmart.ProductClickListner
import com.example.shopsmart.R
import com.example.shopsmart.databinding.ProductItemBinding
import com.example.shopsmart.modelClass.ProductModel

class AllProductAdapter(private var productList: List<ProductModel>, private var listener: ProductClickListner) : RecyclerView.Adapter<AllProductAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    fun updateList(newList: List<ProductModel>) {
        productList = newList
        notifyDataSetChanged()
    }
}