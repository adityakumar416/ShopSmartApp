package com.example.shopsmart.modelClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0, // Changed from String to Double
    var productQuantity: Int = 0, // Changed from String to Int
    var favoriteProduct: Boolean = false,
    val rating: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String? = null
) : Parcelable
