package com.example.shopsmart.addDeliveryAddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.shopsmart.MainActivity
import com.example.shopsmart.databinding.FragmentUpdateAddressBinding
import com.example.shopsmart.viewModel.MainViewModel

class UpdateAddressFragment : Fragment() {

    private lateinit var binding: FragmentUpdateAddressBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val args: UpdateAddressFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateAddressBinding.inflate(inflater, container, false)
        (activity as MainActivity).setBottomNavigationVisibility(false)

        // Set initial data
        val address = args.updateAddress
        binding.etName.setText(address.name)
        binding.etPhone.setText(address.phone)
        binding.etPincode.setText(address.pincode)
        binding.etAddress.setText(address.address)
        binding.etCity.setText(address.city)
        binding.etState.setText(address.state)
        binding.etLandmark.setText(address.landmark)

        binding.btnSaveAddress.setOnClickListener {
            saveUpdatedAddress(address.id)
        }

        mainViewModel.saveStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Address Update successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Failed to Update address", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }

    private fun saveUpdatedAddress(addressId: String) {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val pincode = binding.etPincode.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val state = binding.etState.text.toString().trim()
        val landmark = binding.etLandmark.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || pincode.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val updatedAddress = AddressModel(
            id = addressId,
            name = name,
            phone = phone,
            pincode = pincode,
            address = address,
            city = city,
            state = state,
            landmark = landmark
        )

        mainViewModel.updateAddress(updatedAddress)

    }
}
