package com.example.shopsmart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopsmart.ProductClickListner
import com.example.shopsmart.R
import com.example.shopsmart.databinding.CartItemBinding
import com.example.shopsmart.interfaces.QuantityChangeListener
import com.example.shopsmart.modelClass.ProductModel

class CartAdapter(
    private var products: List<ProductModel>,
    private val quantityChangeListener: QuantityChangeListener,
    private val productClickListner: ProductClickListner
) : RecyclerView.Adapter<CartAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductModel) {
            binding.productName.text = product.name
            binding.productPrice.text = "₹ ${product.price}"
            binding.itemQuantity.text = product.productQuantity.toString()

           // binding.productImage.setImageResource(R.drawable.item_1)
            Glide.with(binding.productImage.context).load(product.imageUrl).into(binding.productImage)

            binding.totalProductPrice.text = "₹ ${product.price * product.productQuantity}"

            binding.plusIcon.setOnClickListener {
                val newQuantity = product.productQuantity + 1
                quantityChangeListener.onQuantityChanged(product, newQuantity)
            }

            binding.minusIcon.setOnClickListener {
                if (product.productQuantity >= 0) {
                    val newQuantity = product.productQuantity - 1
                    quantityChangeListener.onQuantityChanged(product, newQuantity)
                }
            }
            binding.root.setOnClickListener {
                productClickListner.onClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
