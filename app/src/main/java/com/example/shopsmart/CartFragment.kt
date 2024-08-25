package com.example.shopsmart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsmart.adapter.CartAdapter
import com.example.shopsmart.databinding.FragmentCartBinding
import com.example.shopsmart.interfaces.QuantityChangeListener
import com.example.shopsmart.modelClass.ProductModel
import com.example.shopsmart.viewModel.MainViewModel

class CartFragment : Fragment(), ProductClickListner, QuantityChangeListener {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)

        // Show BottomNavigationView
        (activity as MainActivity).setBottomNavigationVisibility(true)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CartAdapter(emptyList(), this, this)
        binding.recyclerView.adapter = adapter

        // Initial visibility settings
        binding.productProgressBar.visibility = View.VISIBLE
        binding.nestedScrollView.visibility = View.GONE
        binding.emptyCart.visibility = View.GONE
        binding.buyNow.visibility = View.GONE

        // Observe cart items and update UI
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                binding.nestedScrollView.visibility = View.GONE
                binding.emptyCart.visibility = View.VISIBLE
                binding.buyNow.visibility = View.GONE
                binding.productProgressBar.visibility = View.GONE
            } else {
                binding.buyNow.visibility = View.VISIBLE
                binding.emptyCart.visibility = View.GONE
                binding.nestedScrollView.visibility = View.VISIBLE
                binding.productProgressBar.visibility = View.GONE
                adapter.updateData(items)
                updatePriceDetails(items)
            }
        }

        // Observe selected address
        viewModel.selectedAddress.observe(viewLifecycleOwner) { address ->
            address?.let {
                binding.address.text = "${it.address}, ${it.city}, ${it.state}, ${it.pincode}"
            }
        }

        // Set up listeners
        binding.paymentMethod.setOnClickListener {
            showPaymentMethodDialog()
        }
        binding.deliveryAddress.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_viewAllAddressFragment)
        }

        // **Observe selected payment method**
        viewModel.selectedPaymentMethod.observe(viewLifecycleOwner) { method ->
            binding.paymentMode.text = method
        }

        // Handle back button click
        binding.backIcon.setOnClickListener {
            findNavController().navigateUp() // Navigate back to the previous screen
        }

        // Fetch cart items and selected address
        viewModel.fetchCartItems()
        viewModel.loadSelectedAddressFromFirestore()

        return binding.root
    }

    private fun showPaymentMethodDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_payment_method, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroupPaymentMethods)

        // Preselect the current payment method or "Cash on Delivery" by default
        when (viewModel.selectedPaymentMethod.value) {
            "Cash on Delivery" -> radioGroup.check(R.id.radioCashOnDelivery)
            "Credit Card" -> radioGroup.check(R.id.radioCreditCard)
            "Debit Card" -> radioGroup.check(R.id.radioDebitCard)
            "UPI" -> radioGroup.check(R.id.radioUPI)
            // Add more methods if necessary
        }

        val dialog = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Select Payment Method")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    // Get the selected payment method
                    val selectedMethod = dialogView.findViewById<RadioButton>(
                        radioGroup.checkedRadioButtonId
                    ).text.toString()

                    // Save the selected payment method in ViewModel
                    viewModel.savePaymentMethod(selectedMethod)

                }
                .setNegativeButton("Cancel", null)
                .create()
        }

        dialog?.show()
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
        // Implement favorite functionality if needed
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
