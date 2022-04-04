package com.example.sofront

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.sofront.databinding.ActivityNumAuthBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class NumAuth : AppCompatActivity() {
    val auth = Firebase.auth
    var verificationId = ""

    var UID:String = ""

    private fun signInWithPhoneAuthCredential(credential : PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                Toast.makeText(this, "인증에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DetailInfoActivity::class.java)
                intent.putExtra("UID", UID)

                val UIDjson = UID(UID)
                RetrofitService._postAuth(UIDjson)

                //전화번호 인증 삭제
                val user = Firebase.auth.currentUser!!
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User account deleted.")
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            this.finish()
                        }
                    }
            }else {
                Toast.makeText(this, "인증에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNumAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UID = intent.getStringExtra("UID").toString()

        var authCompleted = false

        binding.geAuth.setOnClickListener{
            if(binding.numSecondEt.text.toString() != "" && binding.numThirdEt.text.toString() != ""){
                val phoneNum:String = "+8210"+ binding.numSecondEt.text.toString() + binding.numThirdEt.text.toString()
                Toast.makeText(this, "인증번호가 전송되었습니다.\n잠시만 기다려주세요!", Toast.LENGTH_SHORT).show()
                authCompleted = true

                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) { }
                    override fun onVerificationFailed(e: FirebaseException) {
                    }
                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        this@NumAuth.verificationId = verificationId
                    }
                }
                //120초동안 버튼 다시 눌러도 재전송 안됨. 근데 이부분을 사용자가 알게해야될 것 같은데.. 어떻게 하지
                val optionsCompat =  PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNum)
                    .setTimeout(120L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(optionsCompat)
                auth.setLanguageCode("kr")
            }else{
                Toast.makeText(this, "유효하지 않은 번호입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.complete.setOnClickListener{
            if(authCompleted){
                if(binding.authEt.text.toString() == ""){
                    Toast.makeText(this,"인증번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else {
                    val credential = PhoneAuthProvider.getCredential(
                        verificationId,
                        binding.authEt.text.toString()
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }else{
                Toast.makeText(this, "인증이 안료되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}