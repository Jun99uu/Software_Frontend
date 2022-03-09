package com.example.sofront

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.sofront.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩 예제 기존에 작성되어있는 setContentView 지우고 이런식으로 작성해주쎄여
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 메인 액티비티는 내가 작성할게 따로 empty activity 파서 해주
        // intent는 마지막에 다 하고 연결하면 되니까 따로 작성할 필요는 없구
        // 코드 테스트 할 때 intent 사용하면, 깃에 업로드할 때는 그 부분 지우고 push 해주세영
        // 화이팅 (●'◡'●)

        val fragmentList = listOf(IntroFragment1(), IntroFragment2(), IntroFragment3(), IntroFragment4(), IntroFragment5())
        val adapter = IntroViewPagerAdapter(this)
        adapter.fragments = fragmentList
        binding.intro.adapter = adapter
        binding.wormDotsIndicator.setViewPager2(binding.intro)

        binding.intro.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 4){
                    var btShape : GradientDrawable = binding.withBtn.background as GradientDrawable
                    btShape.setColor(Color.parseColor("#FFB2A6"))
                }else if(position == 3){
                    var btShape : GradientDrawable = binding.withBtn.background as GradientDrawable
                    Log.d("$position :", btShape.toString())
                    btShape.setColor(Color.parseColor("#F9F9F9"))
                }
            }
        })

        binding.withBtn.setOnClickListener{
            val bottomSheet = SignInBottomSheet()
            bottomSheet.show(supportFragmentManager, SignInBottomSheet.TAG)
        }

    }
}