package com.example.shopsmart.modelClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val id: String = "",
    val name: String = "",
    val price: String = "",
    val rating: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String? = null
): Parcelable

