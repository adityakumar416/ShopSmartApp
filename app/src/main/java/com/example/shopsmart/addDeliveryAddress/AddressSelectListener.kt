package com.example.shopsmart.addDeliveryAddress


interface AddressSelectListener {
    fun onAddressSelected(address: AddressModel)
    fun onAddressEditRequested(address: AddressModel)
}

