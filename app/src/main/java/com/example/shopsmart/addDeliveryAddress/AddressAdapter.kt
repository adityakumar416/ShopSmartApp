package com.example.shopsmart.addDeliveryAddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopsmart.databinding.AddressItemBinding

class AddressAdapter(
    private var addresses: List<AddressModel>,
    private var selectedAddress: AddressModel?,
    private val listener: AddressSelectListener
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var selectedPosition: Int = addresses.indexOfFirst { it.id == selectedAddress?.id }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addresses[position]
        holder.bind(address)

        holder.binding.radioButton.isChecked = position == selectedPosition

        holder.binding.radioButton.setOnClickListener {
            if (selectedPosition != position) {
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(selectedPosition)
                listener.onAddressSelected(address)
            }
        }

        holder.binding.tvAddress.setOnClickListener {
            listener.onAddressEditRequested(address)
        }
    }

    override fun getItemCount(): Int = addresses.size

    // Method to update the list of addresses
    fun updateData(newAddresses: List<AddressModel>) {
        this.addresses = newAddresses
        selectedPosition = addresses.indexOfFirst { it.id == selectedAddress?.id }
        notifyDataSetChanged()
    }

    fun updateSelectedAddress(selectedAddress: AddressModel?) {
        this.selectedAddress = selectedAddress
        selectedPosition = addresses.indexOfFirst { it.id == selectedAddress?.id }
        notifyDataSetChanged()
    }

    inner class AddressViewHolder(val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(address: AddressModel) {
            binding.tvAddress.text = "${address.name}\n${address.address}, ${address.landmark}\n${address.city}, ${address.state} - ${address.pincode}"
        }
    }
}
