package com.example.shopsmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopsmart.adapter.ProductAdapter
import com.example.shopsmart.databinding.FragmentWishlistBinding
import com.example.shopsmart.modelClass.ProductModel
import com.example.shopsmart.viewModel.MainViewModel

class WishlistFragment : Fragment() {
    private lateinit var binding: FragmentWishlistBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistBinding.inflate(layoutInflater, container, false)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        productAdapter = ProductAdapter(emptyList(), object : ProductClickListner {
            override fun onClick(product: ProductModel) {
                val action = WishlistFragmentDirections.actionWishlistFragmentToProductDetailsFragment(product)
                findNavController().navigate(action)
            }

            override fun onFavouriteClick(productModel: ProductModel) {
                mainViewModel.updateProductFavoriteStatus(productModel)
                mainViewModel.fetchProducts()
            }
        })
        binding.recyclerView.adapter = productAdapter

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
                productAdapter.updateData(favoriteProducts)
            }
            binding.wishListProgressBar.visibility = View.GONE
        }


        return binding.root
    }

}
