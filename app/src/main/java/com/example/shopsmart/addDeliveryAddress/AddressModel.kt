package com.example.shopsmart.addDeliveryAddress

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressModel(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val pincode: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val landmark: String = "",
    var selectedAddress: Boolean = false // New property to track the selected state
) : Parcelable
