package com.example.sofront

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sofront.databinding.FragmentSignInBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

class SignInBottomSheet : BottomSheetDialogFragment() {
    //프래그먼트에서는 context를 상속받지 않기 때문에 임의로 만들어줌
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    //카카오 로그인 버튼 클릭하고, 가입을 하던 로그인을 하던 토큰 발급받음
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when {
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    Toast.makeText(mainActivity, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(mainActivity, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                    Toast.makeText(mainActivity, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                    Toast.makeText(mainActivity, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                    Toast.makeText(mainActivity, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    Toast.makeText(mainActivity, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    Toast.makeText(mainActivity, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    Toast.makeText(mainActivity, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                }
                else -> { // Unknown
                    Toast.makeText(mainActivity, "기타 에러", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (token != null) {
            //카카오 로그인 버튼을 눌러서 성공. 가입일 수도 있고 그냥 로그인일 수도 있음
            Toast.makeText(mainActivity, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

            //통신을 통해서 회원가입인지, 그냥 로그인인지 파악. 회원가입인 경우를 false라고 하겠음
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (tokenInfo != null) {
                    val UID = tokenInfo.id
                    val checked = false //여기
                    if(!checked){
                        val intent = Intent(mainActivity, DetailInfoActivity::class.java)
                        intent.putExtra("UID", UID.toString()) //회원 고유번호
                        UserApiClient.instance.me { user, error ->
                            intent.putExtra("UName", user?.kakaoAccount?.profile?.nickname) //회원 닉네임(카카오톡 이름)
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            mainActivity.finish()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSignInBottomSheetBinding.inflate(inflater, container, false)
        binding.kakaoBtn.setOnClickListener{
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(mainActivity)){
                UserApiClient.instance.loginWithKakaoTalk(mainActivity, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(mainActivity, callback = callback)
            }
        }
        return binding.root
    }

    //여기 아래는 안봐도됨 _ sheet의 크기 설정하기 위한 부분

    companion object {
        const val TAG = "SignInBottomSheet"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            // 다이얼로그 크기 설정 (인자값 : DialogInterface)
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 65 / 100
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}