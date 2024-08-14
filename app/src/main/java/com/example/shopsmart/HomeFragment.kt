package com.example.shopsmart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.shopsmart.adapter.BannerAdapter
import com.example.shopsmart.databinding.FragmentHomeBinding
import com.example.shopsmart.modelClass.BannerModel
import com.example.shopsmart.viewModel.MainViewModel
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var bannerViewPager: BannerViewPager<BannerModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

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

        mainViewModel.fetchBanners()

        return binding.root
    }
}
