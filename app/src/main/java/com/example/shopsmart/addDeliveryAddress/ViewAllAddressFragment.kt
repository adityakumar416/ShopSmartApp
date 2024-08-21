package com.example.shopsmart.addDeliveryAddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsmart.MainActivity
import com.example.shopsmart.R
import com.example.shopsmart.databinding.FragmentViewAllAddressBinding
import com.example.shopsmart.viewModel.MainViewModel

class ViewAllAddressFragment : Fragment(), AddressSelectListener {

    private lateinit var binding: FragmentViewAllAddressBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var adapter: AddressAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewAllAddressBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        (activity as MainActivity).setBottomNavigationVisibility(false)

        binding.addressProgressBar.visibility = View.VISIBLE

        mainViewModel.loadSelectedAddressFromFirestore()

// Initialize the adapter with empty data and set it to the RecyclerView
        adapter = AddressAdapter(emptyList(), mainViewModel.selectedAddress.value, this)
        binding.recyclerView.adapter = adapter

        // Observe addresses and update UI
        mainViewModel.addresses.observe(viewLifecycleOwner) { addresses ->
            binding.addressProgressBar.visibility = View.GONE
            if (addresses.isEmpty()) {
                Toast.makeText(requireContext(), "No addresses found", Toast.LENGTH_SHORT).show()
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                adapter?.updateData(addresses)
                adapter?.updateSelectedAddress(mainViewModel.selectedAddress.value)
            }
        }

        mainViewModel.fetchAddresses()

        binding.addAddress.setOnClickListener {
            findNavController().navigate(R.id.action_viewAllAddressFragment_to_addDeliveryAddressFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addressProgressBar.visibility = View.VISIBLE

        mainViewModel.loadSelectedAddressFromFirestore()

        // Set up the RecyclerView and LiveData observer
        mainViewModel.addresses.observe(viewLifecycleOwner) { addresses ->
            binding.addressProgressBar.visibility = View.GONE
            if (addresses.isEmpty()) {
                Toast.makeText(requireContext(), "No addresses found", Toast.LENGTH_SHORT).show()
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                if (adapter == null) {
                    adapter = AddressAdapter(addresses, mainViewModel.selectedAddress.value, this)
                    binding.recyclerView.adapter = adapter
                } else {
                    adapter?.updateSelectedAddress(mainViewModel.selectedAddress.value)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
        mainViewModel.fetchAddresses()
    }

    override fun onAddressSelected(address: AddressModel) {
        mainViewModel.selectAddress(address)
    }

    override fun onAddressEditRequested(address: AddressModel) {
        val action = ViewAllAddressFragmentDirections.actionViewAllAddressFragmentToUpdateAddressFragment(address)
        findNavController().navigate(action)
    }

}
