package com.example.shopsmart

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.shopsmart.databinding.FragmentProductDetailsBinding
import com.example.shopsmart.modelClass.OrderModel
import com.example.shopsmart.viewModel.MainViewModel
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private val args: ProductDetailsFragmentArgs by navArgs()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        // Hide BottomNavigationView
        (activity as MainActivity).setBottomNavigationVisibility(false)

        // Setting data from navArgs to views
        binding.productName.text = args.productmodel.name
        binding.productPrice.text = "₹${args.productmodel.price}" // Adding ₹ symbol before the price
        binding.rating.text = "${args.productmodel.rating} Rating"
        binding.productDescription.text = args.productmodel.description
        Glide.with(binding.productImage.context).load(args.productmodel.imageUrl).into(binding.productImage)

        // Set initial favorite icon based on favorite status
        updateFavoriteIcon(args.productmodel.favoriteProduct)

        // Handle back button click
        binding.backIcon.setOnClickListener {
            findNavController().navigateUp() // Navigate back to the previous screen
        }

        binding.shareIcon.setOnClickListener {
            shareProduct()
        }

        binding.buyNow.setOnClickListener {
            placeOrder()
        }

        binding.favoriteIcons.setOnClickListener {
            val isFavorite = !args.productmodel.favoriteProduct
            args.productmodel.favoriteProduct = isFavorite
            mainViewModel.updateProductFavoriteStatus(args.productmodel)
            updateFavoriteIcon(isFavorite)
        }

        binding.cartIcon.setOnClickListener {
            mainViewModel.addToCart(args.productmodel, activity as MainActivity)
        }

        return binding.root
    }


    private fun placeOrder() {
        val orderId = generateUniqueOrderId()
        val orderDate = getCurrentDate()
        val paymentMethod = "Cash on Delivery" // Assuming Cash on Delivery (you can change this as per your payment method selection)
        val orderStatus = "Pending"

        val order = OrderModel(
            id = args.productmodel.id,
            name = args.productmodel.name,
            price = args.productmodel.price,
            productQuantity = args.productmodel.productQuantity,
            favoriteProduct = args.productmodel.favoriteProduct,
            rating = args.productmodel.rating,
            description = args.productmodel.description,
            category = args.productmodel.category,
            imageUrl = args.productmodel.imageUrl,
            orderDate = orderDate,
            orderStatus = orderStatus,
            orderId = orderId,
            paymentMethod = paymentMethod
        )

        mainViewModel.placeOrder(order, activity as MainActivity)


    }

    private fun generateUniqueOrderId(): String {
        val random = Random(System.currentTimeMillis())
        return (10000000 + random.nextInt(90000000)).toString()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.favoriteIcons.setImageResource(
            if (isFavorite) R.drawable.red_hearts else R.drawable.blank_heart_fev_icon
        )
    }

    private fun shareProduct() {
        // Creating the deep link for the product
        val productUrl = "https://www.shopsmart.com/product/${args.productmodel.id}"

        // Preparing the share content
        val shareContent = """
            Check out this product:
            Product Name: ${args.productmodel.name}
            Price: ₹${args.productmodel.price}
            Rating: ${args.productmodel.rating} Rating
            Description: ${args.productmodel.description}
            View Product: $productUrl
        """.trimIndent()

        // Creating the share intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareContent)
        }

        // Starting the share activity
        startActivity(Intent.createChooser(shareIntent, "Share Product via"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setBottomNavigationVisibility(true)
    }
}
