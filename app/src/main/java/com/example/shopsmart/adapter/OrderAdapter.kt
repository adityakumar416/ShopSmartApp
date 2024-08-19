package com.example.shopsmart.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopsmart.R
import com.example.shopsmart.modelClass.OrderModel
import java.text.SimpleDateFormat
import java.util.Locale

class OrderAdapter(private var orderList: List<OrderModel>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orderList.size

    fun updateOrders(newOrders: List<OrderModel>) {
        orderList = newOrders
        notifyDataSetChanged()
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName = itemView.findViewById<TextView>(R.id.product_name)
        private val productPrice = itemView.findViewById<TextView>(R.id.product_price)
        private val orderDate = itemView.findViewById<TextView>(R.id.order_date)
        private val orderId = itemView.findViewById<TextView>(R.id.order_id)
        private val orderStatus = itemView.findViewById<TextView>(R.id.order_status)
        private val paymentMethod = itemView.findViewById<TextView>(R.id.order_payment_mode)
        private val productImage = itemView.findViewById<ImageView>(R.id.product_image)

        fun bind(order: OrderModel) {
            productName.text = order.name
            productPrice.text = "₹${order.price}"
            orderDate.text = "Ordered On ${order.orderDate}"
            orderId.text = "Order ID: ${order.orderId}"
            orderStatus.text = order.orderStatus
            paymentMethod.text = order.paymentMethod
            Glide.with(productImage.context).load(order.imageUrl).into(productImage)



        }
    }
}
