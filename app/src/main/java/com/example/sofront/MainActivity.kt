package com.example.sofront

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.sofront.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


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

//        val tmpIntent = Intent(this, HomeActivity::class.java)
//        startActivity(tmpIntent)

//        Firebase.auth.signOut()
//        RetrofitService._getPlanByUid("test")
//        RetrofitService._getDownloadPlanByUid("류승민")
        val fragmentList = listOf(IntroFragment1(), IntroFragment2(), IntroFragment3(), IntroFragment4(), IntroFragment5())
        val adapter = IntroViewPagerAdapter(this)
        adapter.fragments = fragmentList
        binding.intro.adapter = adapter
        binding.wormDotsIndicator.setViewPager2(binding.intro)

        val user = Firebase.auth.currentUser
        if(user != null){
            val uid = user.uid
            Firebase.auth.signOut() //임시 로그아웃
            // <통신> 여기서 우리 서버에 해당 uid를 가진 db가 있는지 확인. 있으면 바로 메인화면으로 넘어가고, 없으면 인증페이지(NumAuth)로 넘어감
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this, "로그인 및 회원가입을 진행해주세요.", Toast.LENGTH_SHORT).show()
        }

        binding.intro.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 4){
                    val btShape : GradientDrawable = binding.withBtn.background as GradientDrawable
                    btShape.setColor(Color.parseColor("#FFB2A6"))
                }else if(position == 3){
                    val btShape : GradientDrawable = binding.withBtn.background as GradientDrawable
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

    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 1500) {
            ActivityCompat.finishAffinity(this) // 액티비티를 종료하고
            System.exit(0) // 프로세스를 종료
            return
        }

        // 처음 클릭 메시지
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}