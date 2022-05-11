package com.example.sofront

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.sofront.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileImg.translationZ = 1f

        val prevPadding:Int = Math.round(resources.displayMetrics.density * 30) //30dp 변환값

        binding.profileScrollView.run {
            header = binding.header
            stickListener = { _ ->
                Log.d("스틱헤더 : ", "붙었다")
                binding.portfolioBtn.setPadding(0,0,0,0)
                binding.madePlanBtn.setPadding(0,0,0,0)
            }
            freeListener = { _ ->
                Log.d("스틱헤더 : ", "떨어졌다")
                binding.portfolioBtn.setPadding(0,0,prevPadding,0)
                binding.madePlanBtn.setPadding(prevPadding,0,0,0)
            }
        }

        val fragmentList = listOf(ProfilePortfolioFragment(), ProfilePlanFragment())
        val adapter = ProfileVPAdapter(this)
        adapter.fragments = fragmentList
        binding.profileMainSheet.adapter = adapter

        binding.profileMainSheet.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0){
                    val pfShape : GradientDrawable = binding.portfolioBtn.background as GradientDrawable
                    pfShape.setColor(Color.parseColor("#61A4BC"))
                    val plShape : GradientDrawable = binding.madePlanBtn.background as GradientDrawable
                    plShape.setColor(Color.parseColor("#3F3F3F"))
                }else{
                    val plShape : GradientDrawable = binding.madePlanBtn.background as GradientDrawable
                    plShape.setColor(Color.parseColor("#61A4BC"))
                    val pfShape : GradientDrawable = binding.portfolioBtn.background as GradientDrawable
                    pfShape.setColor(Color.parseColor("#3F3F3F"))
                }
            }
        })

        binding.portfolioBtn.setOnClickListener{
            binding.profileMainSheet.setCurrentItem(0, true)
        }
        binding.madePlanBtn.setOnClickListener{
            binding.profileMainSheet.setCurrentItem(1, true)
        }
    }
}