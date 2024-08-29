package com.example.shopsmart

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopsmart.adapter.BannerAdapter
import com.example.shopsmart.adapter.ProductAdapter
import com.example.shopsmart.databinding.FragmentHomeBinding
import com.example.shopsmart.modelClass.BannerModel
import com.example.shopsmart.modelClass.ProductModel
import com.example.shopsmart.viewModel.MainViewModel
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

class HomeFragment : Fragment(), ProductClickListner {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var bannerViewPager: BannerViewPager<BannerModel>
    private lateinit var productAdapter: ProductAdapter
    private var scrollPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavigationVisibility(true)

        binding.textView9.setOnClickListener {
            navigateToCategory("AllProducts")
        }

        binding.viewAll.setOnClickListener {
            navigateToCategory("AllProducts")
        }

        binding.cloths.setOnClickListener {
            navigateToCategory("Cloths")
        }

        binding.digitals.setOnClickListener {
            navigateToCategory("Digital")
        }
        binding.tools.setOnClickListener {
            navigateToCategory("Tools")
        }

        binding.beauty.setOnClickListener {
            navigateToCategory("Beauty")
        }

        // Initialize BannerViewPager
        bannerViewPager = binding.root.findViewById(R.id.banner_view)
        bannerViewPager.apply {
            setScrollDuration(900)
            setInterval(4600)
            setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
            registerLifecycleObserver(lifecycle)
            adapter = BannerAdapter()
        }

        binding.progressBar.visibility = View.VISIBLE
        mainViewModel.bannerList.observe(viewLifecycleOwner, Observer { banners ->
            if (!banners.isNullOrEmpty()) {
                bannerViewPager.create(banners)
            }
            binding.progressBar.visibility = View.GONE
        })

        // Get the screen orientation and set the number of columns
        val spanCount =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerView.setHasFixedSize(true)

        productAdapter = ProductAdapter(emptyList(), this)
        binding.recyclerView.adapter = productAdapter

        binding.productProgressBar.visibility = View.VISIBLE
        mainViewModel.productList.observe(viewLifecycleOwner) { products ->
            productAdapter.updateData(products)
            binding.productProgressBar.visibility = View.GONE
        }
        mainViewModel.fetchProducts(null)
        mainViewModel.fetchBanners()

        return binding.root
    }


    fun navigateToCategory(category: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToViewAllProductFragment(category)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore the scroll position if available
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("scroll_position")
            binding.nestedScrollView.post {
                binding.nestedScrollView.scrollTo(0, scrollPosition)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the current scroll position if _binding is not null
        _binding?.let {
            scrollPosition = it.nestedScrollView.scrollY
            outState.putInt("scroll_position", scrollPosition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(product: ProductModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }

    override fun onFavouriteClick(productModel: ProductModel) {
        mainViewModel.updateProductFavoriteStatus(productModel)
    }
}
