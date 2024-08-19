package com.example.shopsmart.interfaces

import com.example.shopsmart.modelClass.ProductModel

interface QuantityChangeListener {
    fun onQuantityChanged(product: ProductModel, newQuantity: Int)
}
