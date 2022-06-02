package com.example.sofront

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.sofront.databinding.ActivityPlanDetailViewBinding
import com.example.sofront.databinding.ActivitySettingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser!!
    val defaultImg = R.drawable.gymdori
    private var mBinding: ActivitySettingBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _getCertainProfile(user.uid)

        binding.signOutBtn.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            Toast.makeText(this, "다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.changePwdBtn.setOnClickListener{
            val email = user.email.toString()
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "설정된 이메일로 비밀번호 변경 메일이 전송되었어요.", Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.resignBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("짐꾼을 탈퇴하시겠습니까?")
                .setMessage("삭제된 계정은 복구되지 않습니다.\n신중하게 결정해주세요.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        user.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this, MainActivity::class.java)
                                    Toast.makeText(this, "언제든 다시 돌아와주세요🙂", Toast.LENGTH_SHORT).show()
                                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                }
                            }
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(this, "우리 조금 더 오래 봐요😍", Toast.LENGTH_SHORT).show()
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }

    fun _getCertainProfile(UID:String){
        var certainProfile:certainProfile
        RetrofitService.retrofitService.getCertainProfile(UID).enqueue(object :
            Callback<certainProfile> {
            override fun onResponse(call: Call<certainProfile>, response: Response<certainProfile>) {
                if (response.isSuccessful) {
                    Log.d("getProfile in Plan test", "success")
                    Log.d("getProfile in Plan success", response.body().toString())
                    certainProfile = response.body()!!
                    Log.d("프로필", certainProfile.toString())
                    binding.userName.text = certainProfile.name
                    openImg(certainProfile.profileImg)
                } else {
                    Log.d("getProfile in Plan test", "success but something error")
                }
            }

            override fun onFailure(call: Call<certainProfile>, t: Throwable) {
                Log.d("getProfile in Plan test", "fail")
            }
        })
    }

    fun openImg(profileImg:String){
        Glide.with(this)
            .load(profileImg) // 불러올 이미지 url
            .placeholder(defaultImg) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImg) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImg) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(binding.profileImage) // 이미지를 넣을 뷰
    }
}