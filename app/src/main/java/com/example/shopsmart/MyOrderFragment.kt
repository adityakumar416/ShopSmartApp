package com.example.shopsmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsmart.adapter.OrderAdapter
import com.example.shopsmart.databinding.FragmentMyorderBinding
import com.example.shopsmart.interfaces.OrderClickListner
import com.example.shopsmart.modelClass.OrderModel
import com.example.shopsmart.viewModel.MainViewModel

class MyOrderFragment : Fragment(), OrderClickListner {

    private lateinit var binding: FragmentMyorderBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var orderAdapter: OrderAdapter
    private var selectedChipId: Int = R.id.chip_all // Variable to store the selected chip ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyorderBinding.inflate(layoutInflater, container, false)

        setupRecyclerView()
        observeOrders()

        // Restore the previously selected chip
        binding.chipGroup.check(selectedChipId)

        // Apply the filter based on the saved chip state
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedChipId = checkedId // Save the selected chip ID
            filterOrdersBasedOnChip(checkedId)
        }

        // Apply the filter based on the selected chip ID
        filterOrdersBasedOnChip(selectedChipId)

        viewModel.fetchOrders() // Fetch orders when fragment is created

        return binding.root
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(emptyList(), this)
        binding.recyclerView.adapter = orderAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeOrders() {
        viewModel.orderList.observe(viewLifecycleOwner) { orders ->
            if (orders.isNullOrEmpty()) {
                binding.emptyCart.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyCart.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                orderAdapter.updateOrders(orders)
            }
        }
    }

    private fun filterOrdersBasedOnChip(chipId: Int) {
        when (chipId) {
            R.id.chip_all -> viewModel.filterOrders("All")
            R.id.chip_complete -> viewModel.filterOrders("Completed")
            R.id.chip_cancelled -> viewModel.filterOrders("Cancelled")
            R.id.chip_pending -> viewModel.filterOrders("Pending")
        }
    }

    override fun onOrderClick(orderModel: OrderModel) {
        findNavController().navigate(R.id.action_myOrderFragment_to_orderStatusFragment)
    }
}
