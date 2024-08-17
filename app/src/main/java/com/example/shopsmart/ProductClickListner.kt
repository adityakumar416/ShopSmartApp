package com.example.shopsmart

import com.example.shopsmart.modelClass.ProductModel

interface ProductClickListner {

    fun onClick(productModel: ProductModel)
}