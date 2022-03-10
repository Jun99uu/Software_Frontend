package com.example.sofront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sofront.databinding.ActivityNumAuthBinding
import com.example.sofront.databinding.ActivitySignUpAuthBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class NumAuth : AppCompatActivity() {
    val auth = Firebase.auth
    var verificationId = ""

    val ID = intent.getStringExtra("ID")
    val PWD = intent.getStringExtra("PWD")
    val Email = intent.getStringExtra("Email")

    private fun signInWithPhoneAuthCredential(credential : PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                Toast.makeText(this, "인증에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DetailInfoActivity::class.java)
                intent.putExtra("ID", ID)
                intent.putExtra("PWD", PWD)
                intent.putExtra("Email", Email)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                this.finish()
            }else {
                Toast.makeText(this, "인증에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNumAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var authCompleted:Boolean = false

        binding.geAuth.setOnClickListener{
            if(binding.numSecondEt.text.toString() != "" && binding.numThirdEt.text.toString() != ""){
                val phoneNum:String = "+8210"+ binding.numSecondEt.text.toString() + binding.numThirdEt.text.toString()
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
                val credential = PhoneAuthProvider.getCredential(verificationId, binding.authEt.text.toString())
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this, "인증이 안료되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}