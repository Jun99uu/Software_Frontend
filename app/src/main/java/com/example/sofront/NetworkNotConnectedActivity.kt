package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sofront.databinding.ActivityNetworkNotConnectedBinding

class NetworkNotConnectedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNetworkNotConnectedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}