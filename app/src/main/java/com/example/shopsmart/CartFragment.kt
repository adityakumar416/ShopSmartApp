package com.example.shopsmart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsmart.adapter.CartAdapter
import com.example.shopsmart.databinding.FragmentCartBinding
import com.example.shopsmart.interfaces.QuantityChangeListener
import com.example.shopsmart.modelClass.ProductModel
import com.example.shopsmart.viewModel.MainViewModel

class CartFragment : Fragment(), ProductClickListner,QuantityChangeListener {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CartAdapter(emptyList(), this,this)
        binding.recyclerView.adapter = adapter

        binding.productProgressBar.visibility = View.VISIBLE
        binding.nestedScrollView.visibility = View.GONE
        binding.emptyCart.visibility = View.GONE
        binding.buyNow.visibility = View.GONE

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                binding.nestedScrollView.visibility = View.GONE
                binding.emptyCart.visibility = View.VISIBLE
                binding.buyNow.visibility = View.GONE
                binding.productProgressBar.visibility = View.GONE

            }else {
                binding.buyNow.visibility = View.VISIBLE
                binding.emptyCart.visibility = View.GONE
                binding.nestedScrollView.visibility = View.VISIBLE
                binding.productProgressBar.visibility = View.GONE
                adapter.updateData(items)
                updatePriceDetails(items)
            }
        }

        binding.paymentMethod.setOnClickListener {
            Toast.makeText(context, "Payment Method is On Working", Toast.LENGTH_SHORT).show()
        }
        binding.deliveryAddress.setOnClickListener {
            Toast.makeText(context, "Delivery Address is On Working", Toast.LENGTH_SHORT).show()
        }

        // Fetch cart items
        viewModel.fetchCartItems()

        return binding.root
    }

    private fun updatePriceDetails(items: List<ProductModel>) {
        val subtotal = items.sumOf { it.price * it.productQuantity }
        val deliveryCharge = 40
        val tax = 10
        val totalPrice = subtotal + deliveryCharge + tax

        // Update UI
        binding.subtotalPrice.text = "₹$subtotal"
        binding.deliveryPrice.text = "₹$deliveryCharge"
        binding.totalTexPrice.text = "₹$tax"
        binding.totalPrice.text = "₹$totalPrice"
    }

    override fun onClick(productModel: ProductModel) {
        val action = CartFragmentDirections.actionCartFragmentToProductDetailsFragment(productModel)
        findNavController().navigate(action)
    }

    override fun onFavouriteClick(productModel: ProductModel) {

    }

    override fun onQuantityChanged(product: ProductModel, newQuantity: Int) {
        if (newQuantity <= 0) {
            viewModel.removeCartItem(product)
        } else {
            product.productQuantity = newQuantity
            viewModel.updateCartItem(product)
        }
        viewModel.fetchCartItems()
    }
}
