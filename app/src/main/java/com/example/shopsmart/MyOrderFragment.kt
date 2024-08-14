package com.example.shopsmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopsmart.databinding.FragmentCartBinding
import com.example.shopsmart.databinding.FragmentMyorderBinding


class MyOrderFragment : Fragment() {
    private lateinit var binding: FragmentMyorderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyorderBinding.inflate(layoutInflater, container, false)





        return binding.root
    }


}