package com.example.shopsmart.interfaces

import com.example.shopsmart.modelClass.OrderModel

interface OrderClickListner {
    fun onOrderClick(orderModel: OrderModel)
}