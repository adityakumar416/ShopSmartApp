package com.example.shopsmart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shopsmart.databinding.FragmentProfileBinding
import com.example.shopsmart.viewModel.MainViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavigationVisibility(true)

        // Initialize the list with 10 demo items
        val demoItems = listOf(
            CardItem("1", "Edit Profile", R.drawable.profile_icon),
            CardItem("2", "Shopping Address", R.drawable.location),
            CardItem("3", "Wishlist", R.drawable.heart_icon),
            CardItem("4", "Order History", R.drawable.list_icon),
            CardItem("5", "Notification", R.drawable.notification_icon),
            CardItem("6", "Payment Methods", R.drawable.credit_card),
            CardItem("7", "Share App", R.drawable.share_icon),
            CardItem("8", "Contact Us", R.drawable.phone_icon),
            CardItem("9", "Change Password", R.drawable.change_password),
            CardItem("10", "Logout", R.drawable.logout_icon)
        )

        // Add items to the LinearLayout programmatically
        demoItems.forEach { cardItem ->
            addProfileItem(cardItem)
        }


        // Handle back button click
        binding.backIcon.setOnClickListener {
            findNavController().navigateUp() // Navigate back to the previous screen
        }

        return binding.root
    }

    private fun addProfileItem(cardItem: CardItem) {
        val itemView = LayoutInflater.from(requireContext())
            .inflate(R.layout.profile_list_items, binding.linearLayout, false) as RelativeLayout

        // Set the item image
        val imageView = itemView.findViewById<ImageView>(R.id.feature_image)
        imageView.setImageResource(cardItem.imageResId)

        // Set the item title
        val textView = itemView.findViewById<TextView>(R.id.feature_text)
        textView.text = cardItem.title

        // Add click listener to the item
        itemView.setOnClickListener {
            // Handle different actions based on cardItem.id
            when (cardItem.id) {
                "1" -> handleEditProfileClick()
                "2" -> handleShoppingAddressClick()
                "3" -> handleWishlistClick()
                "4" -> handleOrderHistoryClick()
                "5" -> handleNotificationClick()
                "6" -> handlePaymentMethodsClick()
                "7" -> handleShareAppClick()
                "8" -> handleContactUsClick()
                "9" -> handleChangePasswordClick()
                "10" -> handleLogoutClick()
                else -> Toast.makeText(requireContext(), "Unknown action", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Add the item to the LinearLayout
        binding.linearLayout.addView(itemView)
    }

    private fun handleEditProfileClick() {
        Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show()
    }

    private fun handleShoppingAddressClick() {
        findNavController().navigate(R.id.action_profileFragment_to_viewAllAddressFragment)
    }

    private fun handleWishlistClick() {
        findNavController().navigate(R.id.action_profileFragment_to_wishlistFragment)
    }

    private fun handleOrderHistoryClick() {
        findNavController().navigate(R.id.action_profileFragment_to_myOrderFragment)
    }

    private fun handleNotificationClick() {
        Toast.makeText(requireContext(), "Notification clicked", Toast.LENGTH_SHORT).show()
    }

    private fun handlePaymentMethodsClick() {
        showPaymentMethodDialog()
    }

    private fun handleShareAppClick() {
        // Share the app functionality
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this amazing app!")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey, I found this amazing app called ShopSmart. You should check it out! [App Link Here]"
        )
        startActivity(Intent.createChooser(shareIntent, "Share App via"))
    }

    private fun handleContactUsClick() {
        // Call a number
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:8979116063")
        startActivity(dialIntent)
    }

    private fun handleChangePasswordClick() {
        Toast.makeText(requireContext(), "Change Password clicked", Toast.LENGTH_SHORT).show()
    }

    private fun handleLogoutClick() {
        Toast.makeText(requireContext(), "Logout clicked", Toast.LENGTH_SHORT).show()
    }

    data class CardItem(val id: String, val title: String, val imageResId: Int)


    private fun showPaymentMethodDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_payment_method, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroupPaymentMethods)

        // Preselect the current payment method or "Cash on Delivery" by default
        when (mainViewModel.selectedPaymentMethod.value) {
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
                    mainViewModel.savePaymentMethod(selectedMethod)

                }
                .setNegativeButton("Cancel", null)
                .create()
        }

        dialog?.show()
    }

}
