package com.example.shopsmart.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.shopsmart.R
import com.example.shopsmart.modelClass.BannerModel
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

class BannerAdapter  : BaseBannerAdapter<BannerModel>() {

    override fun bindData(holder: BaseViewHolder<BannerModel>, data: BannerModel?, position: Int, pageSize: Int) {
        val imageStart: ImageView = holder.findViewById(R.id.imageSlider)

        Glide.with(imageStart.context).load(data?.url).into(imageStart)

    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_slider;
    }
}