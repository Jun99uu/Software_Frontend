package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sofront.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomBar.setItemSelected(R.id.home)
        openHomeFragment()

        binding.bottomBar.setOnItemSelectedListener {
            when(it){
                R.id.home -> {
                    openHomeFragment()
                }
                R.id.list -> {
                    val listFragment = ListFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, listFragment).commit()
                }
                R.id.heart -> {
                    val heartFragment = HeartFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, heartFragment).commit()
                }
                R.id.profile -> {
                    val profileFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, profileFragment).commit()
                }
            }
        }
    }

    private fun openHomeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, HomeFragment())
        transaction.commit()
    }
}