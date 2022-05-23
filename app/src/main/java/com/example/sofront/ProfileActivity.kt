package com.example.sofront

import android.content.Intent
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
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

        if(true){
            //현재 uid와 들어온 프로필의 uid가 같다면
            binding.suboreditBtn.text = "편집"
        }
        binding.suboreditBtn.setOnClickListener{
            if(true){
                val intent = Intent(this, EditProfileActivity::class.java)
                //프로필사진, 배경사진(비트맵) _ 보고있는 프로필의 uid, 이름, 소개글(String)으로 넘겨줘야함.
                intent.putExtra("nickname", "임시용")
                intent.putExtra("subtitle", "하이 방가방가데스요")
                startActivity(intent)
            }else{
                //구독
            }
        }

        val fragmentList = listOf(ProfilePortfolioFragment(), ProfilePlanFragment())
        val adapter = ProfileVPAdapter(this)
        adapter.fragments = fragmentList
        binding.profileMainSheet.adapter = adapter

        binding.portfolioBtn.setOnClickListener{
            binding.profileMainSheet.setCurrentItem(0, true)
        }
        binding.madePlanBtn.setOnClickListener{
            binding.profileMainSheet.setCurrentItem(1, true)
        }

        binding.profileMainSheet.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val view = (binding.profileMainSheet[0] as RecyclerView).layoutManager?.findViewByPosition(position)
                view?.post {
                    val wMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)
                    if (binding.profileMainSheet.layoutParams.height != view.measuredHeight) {
                        binding.profileMainSheet.layoutParams = (binding.profileMainSheet.layoutParams).also { lp ->
                            lp.height = view.measuredHeight
                        }
                    }
                }
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
    }

}