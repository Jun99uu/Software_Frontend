package com.example.sofront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.example.sofront.databinding.ActivityMain2Binding
import com.example.sofront.databinding.ActivityMainBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE )
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //카카오 로그인 버튼 클릭하고, 가입을 하던 로그인을 하던 토큰 발급받음
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
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