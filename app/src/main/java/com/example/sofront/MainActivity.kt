package com.example.sofront

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sofront.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient

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
//        키 해시값 구하기 위한 코드
//        val keyHash = Utility.getKeyHash(this)
//        Log.d("Hash", keyHash)

        //앱 실행시 자동 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (tokenInfo != null) { //토큰이 있다면
                val UID = tokenInfo.id //회원번호 이걸로 통신해서 있으면 true 없으면 false
                val intent = Intent(this, TmpActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        binding.withBtn.setOnClickListener{
            val bottomSheet = SignInBottomSheet()
            bottomSheet.show(supportFragmentManager, SignInBottomSheet.TAG)
        }

    }
}