package com.example.shopsmart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopsmart.databinding.FragmentWishlistBinding
import com.example.shopsmart.modelClass.ProductModel
import com.example.shopsmart.viewModel.MainViewModel

class WishlistFragment : Fragment() {
    private lateinit var binding: FragmentWishlistBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mixedAdapter: MixedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)

        // Set up GridLayoutManager with span size lookup
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mixedAdapter.getItemViewType(position)) {
                    MixedAdapter.VIEW_TYPE_HORIZONTAL_LIST -> 2 // Span full width for horizontal list
                    MixedAdapter.VIEW_TYPE_GRID_ITEM -> 1 // Span single cell for grid item
                    else -> 1
                }
            }
        }

        // Initialize RecyclerView with GridLayoutManager
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.setHasFixedSize(true)

        // Initialize Mixed Adapter
        mixedAdapter = MixedAdapter(emptyList(), emptyList(), object : ProductClickListner {
            override fun onClick(product: ProductModel) {
                val action = WishlistFragmentDirections.actionWishlistFragmentToProductDetailsFragment(product)
                findNavController().navigate(action)
            }

            override fun onFavouriteClick(productModel: ProductModel) {
                mainViewModel.updateProductFavoriteStatus(productModel)
                mainViewModel.fetchProducts()
            }
        })
        binding.recyclerView.adapter = mixedAdapter

        // Load products and update UI
        binding.wishListProgressBar.visibility = View.VISIBLE
        binding.emptyWishlist.visibility = View.GONE

        mainViewModel.productList.observe(viewLifecycleOwner) { products ->
            val favoriteProducts = products.filter { it.favoriteProduct }
            if (favoriteProducts.isEmpty()) {
                binding.emptyWishlist.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyWishlist.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                mixedAdapter.updateData(favoriteProducts, products)
            }
            binding.wishListProgressBar.visibility = View.GONE
        }

        // Handle back button click
        binding.backIcon.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}
