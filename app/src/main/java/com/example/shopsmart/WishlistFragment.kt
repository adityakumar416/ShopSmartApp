package com.example.shopsmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopsmart.databinding.FragmentCartBinding
import com.example.shopsmart.databinding.FragmentWishlistBinding

class WishlistFragment : Fragment() {
    private lateinit var binding: FragmentWishlistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWishlistBinding.inflate(layoutInflater, container, false)





        return binding.root
    }


}