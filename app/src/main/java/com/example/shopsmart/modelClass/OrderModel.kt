package com.example.shopsmart.modelClass

data class OrderModel(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0, // Changed from String to Double
    var productQuantity: Int = 0, // Changed from String to Int
    var favoriteProduct: Boolean = false,
    val rating: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String? = null,
    val orderDate: String = "",
    val orderStatus: String = "",
    val orderId: String = "",
    val paymentMethod: String = "",
    val orderAddress: String = "",
    val orderItemQuantity: String = "",
    val getDeliveryDate: String = "",
)
