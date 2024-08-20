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
import com.example.shopsmart.databinding.FragmentCartBinding
import com.example.shopsmart.databinding.FragmentMyorderBinding
import com.example.shopsmart.interfaces.OrderClickListner
import com.example.shopsmart.modelClass.OrderModel
import com.example.shopsmart.viewModel.MainViewModel


class MyOrderFragment : Fragment(),OrderClickListner {

    private lateinit var binding: FragmentMyorderBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyorderBinding.inflate(layoutInflater, container, false)


        setupRecyclerView()
        observeOrders()

        viewModel.fetchOrders() // Fetch orders when fragment is created

        return binding.root
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(emptyList(),this)
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

    override fun onOrderClick(orderModel: OrderModel) {
      findNavController().navigate(R.id.action_myOrderFragment_to_orderStatusFragment)
    }
}
