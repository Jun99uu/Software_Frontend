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

        //카카오 로그인 버튼 클릭하고, 가입을 하던 로그인을 하던 토큰 발급받음
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                //카카오 로그인 버튼을 눌러서 성공. 가입일 수도 있고 그냥 로그인일 수도 있음
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                //통신을 통해서 회원가입인지, 그냥 로그인인지 파악. 회원가입인 경우를 false라고 하겠음
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (tokenInfo != null) {
                        val UID = tokenInfo.id
                        val checked = false
                        if(!checked){
                            val intent = Intent(this, DetailInfoActivity::class.java)
                            intent.putExtra("UID", UID.toString()) //회원 고유번호
                            UserApiClient.instance.me { user, error ->
                                intent.putExtra("UName", user?.kakaoAccount?.profile?.nickname) //회원 닉네임(카카오톡 이름)
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                finish()
                            }
                        }
                    }
                }
            }
        }

        binding.kakaoBtn.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
}