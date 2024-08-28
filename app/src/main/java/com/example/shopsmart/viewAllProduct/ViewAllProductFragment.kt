package com.example.shopsmart.viewAllProduct

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopsmart.MainActivity
import com.example.shopsmart.ProductClickListner
import com.example.shopsmart.databinding.FragmentViewAllProductBinding
import com.example.shopsmart.modelClass.ProductModel
import com.example.shopsmart.viewModel.MainViewModel

class ViewAllProductFragment : Fragment(), ProductClickListner {

    private lateinit var binding: FragmentViewAllProductBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val args: ViewAllProductFragmentArgs by navArgs()
    private var allProductAdapter: AllProductAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewAllProductBinding.inflate(inflater, container, false)

        // Hide bottom navigation
        (activity as MainActivity).setBottomNavigationVisibility(false)

        // Initialize the adapter with an empty list
        allProductAdapter = AllProductAdapter(emptyList(), this)
        binding.recyclerView.adapter = allProductAdapter

        // Set layout manager for RecyclerView based on screen orientation
        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerView.setHasFixedSize(true)

        // Show progress bar while fetching products
        binding.allProductProgressBar.visibility = View.VISIBLE


        // Observe product list changes
        mainViewModel.productList.observe(viewLifecycleOwner, productListObserver)

        // Fetch products based on category argument
        args.category?.let { category ->
            mainViewModel.fetchProducts(category)
        }

        if(args.category == "AllProducts"){
            binding.title.text = "All Products"
        }else{
            binding.title.text = args.category +" Products"

        }
        // Handle back button click
        binding.backIcon.setOnClickListener {
            findNavController().navigateUp() // Navigate back to the previous screen
        }

        return binding.root
    }

    // Observer for product list updates
    private val productListObserver = Observer<List<ProductModel>> { products ->
        // Check if product list is empty
        if (products.isEmpty()) {
            // Show empty list layout
            binding.emptyList.visibility = View.VISIBLE
            binding.recyclerViewLayout.visibility = View.GONE
        } else {
            // Hide empty list layout
            binding.emptyList.visibility = View.GONE
            binding.recyclerViewLayout.visibility = View.VISIBLE
            // Update adapter with new product list
            allProductAdapter?.updateList(products)
        }
        // Hide progress bar once data is loaded
        binding.allProductProgressBar.visibility = View.GONE
    }

    override fun onClick(product: ProductModel) {
        // Navigate to ProductDetailsFragment with the selected product
        val action = ViewAllProductFragmentDirections.actionViewAllProductFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }

    override fun onFavouriteClick(productModel: ProductModel) {
        // Update favorite status of the product
        mainViewModel.updateProductFavoriteStatus(productModel)
    }
}
