package com.example.shopsmart.addDeliveryAddress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shopsmart.MainActivity
import com.example.shopsmart.R
import com.example.shopsmart.databinding.FragmentAddDeliveryAddressBinding
import com.example.shopsmart.viewModel.MainViewModel


class AddDeliveryAddressFragment : Fragment() {
    private lateinit var binding: FragmentAddDeliveryAddressBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddDeliveryAddressBinding.inflate(inflater, container, false)
        (activity as MainActivity).setBottomNavigationVisibility(false)

        binding.btnSaveAddress.setOnClickListener {
            saveAddress()
        }

        mainViewModel.saveStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Address saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to save address", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun saveAddress() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val pincode = binding.etPincode.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val state = binding.etState.text.toString().trim()
        val landmark = binding.etLandmark.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || pincode.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val newAddress = AddressModel(
            name = name,
            phone = phone,
            pincode = pincode,
            address = address,
            city = city,
            state = state,
            landmark = landmark
        )

        mainViewModel.saveAddress(newAddress)
    }


}
