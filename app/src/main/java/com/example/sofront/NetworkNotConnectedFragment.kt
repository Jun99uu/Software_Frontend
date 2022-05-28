package com.example.sofront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sofront.databinding.FragmentNetworkNotConnectedBinding

class NetworkNotConnectedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNetworkNotConnectedBinding.inflate(layoutInflater)
        return binding.root
    }

}