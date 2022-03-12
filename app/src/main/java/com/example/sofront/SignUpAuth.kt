package com.example.sofront

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.sofront.databinding.ActivityMainBinding
import com.example.sofront.databinding.ActivitySignUpAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignUpAuth : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        var pwdCheck:Boolean = false

        var firstAgree:Boolean = false
        var secondAgree:Boolean = false
        var thirdAgree:Boolean = false

        val idPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{3,15}\$"
        val pwdPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,15}\$"

        binding.pwdEt.addTextChangedListener{
            val pattern = Pattern.compile(pwdPattern)
            val matcher = pattern.matcher(binding.pwdEt.text.toString())
            if(matcher.find()){
                if(binding.pwdEt.text.toString() == binding.pwdCkEt.text.toString()){
                    binding.pwdCkTxt.text = "완벽한 비밀번호에요✨"
                    pwdCheck = true
                }else{
                    binding.pwdCkTxt.text = "비밀버호가 일치하지 않습니다."
                    pwdCheck = false
                }
            }else{
                binding.pwdCkTxt.text = "유효하지 않은 비밀번호입니다."
            }
        }

        binding.pwdCkEt.addTextChangedListener{
            val pattern = Pattern.compile(pwdPattern)
            val matcher = pattern.matcher(binding.pwdEt.text.toString())
            if(matcher.find()){
                if(binding.pwdEt.text.toString() == binding.pwdCkEt.text.toString()){
                    binding.pwdCkTxt.text = "완벽한 비밀번호에요✨"
                    pwdCheck = true
                }else{
                    binding.pwdCkTxt.text = "비밀번호가 일치하지 않습니다."
                    pwdCheck = false
                }
            }else{
                binding.pwdCkTxt.text = "유효하지 않은 비밀번호입니다."
            }
        }

        binding.emailIdEt.setOnEditorActionListener{ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                // 키보드 내리기
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.emailIdEt.windowToken, 0)
                handled = true
            }
            handled
        }

        binding.emailIdEt.addTextChangedListener{
            binding.emailCkTxt.text = "정확한 이메일을 입력하시길 권유드립니다."
        }

        var mailArray: Array<String> = arrayOf("@naver.com", "@gmail.com", "@hanmail.net", "@nate.com")
        var mailcom:String
        binding.emailComEt.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("이메일 선택")
                .setItems(mailArray,
                    DialogInterface.OnClickListener { dialog, which ->
                        // 여기서 인자 'which'는 배열의 position을 나타냅니다.
                        binding.emailComEt.text = mailArray[which]
                        mailcom = mailArray[which]
                        binding.emailCkTxt.text = "멋진 이메일이네요👊"
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }


//        binding.getauthBtn.setOnClickListener{
//            val bottomSheet = AuthBottomSheet()
//            bottomSheet.show(supportFragmentManager, AuthBottomSheet.TAG)
//        }

        binding.ageAgree.setOnClickListener{
            firstAgree = !firstAgree
            if(firstAgree){
                binding.ageAgree.background = ContextCompat.getDrawable(this, R.drawable.agree_yes_back)
            }else{
                binding.ageAgree.background = ContextCompat.getDrawable(this, R.drawable.agree_no_back)
            }
        }

        binding.inforAgree.setOnClickListener{
            secondAgree = !secondAgree
            if(secondAgree){
                binding.inforAgree.background = ContextCompat.getDrawable(this, R.drawable.agree_yes_back)
            }else{
                binding.inforAgree.background = ContextCompat.getDrawable(this, R.drawable.agree_no_back)
            }
        }

        binding.alarmAgree.setOnClickListener{
            thirdAgree = !thirdAgree
            if(thirdAgree){
                binding.alarmAgree.background = ContextCompat.getDrawable(this, R.drawable.agree_yes_back)
            }else{
                binding.alarmAgree.background = ContextCompat.getDrawable(this, R.drawable.agree_no_back)
            }
        }


        binding.next.setOnClickListener{
            if(!pwdCheck || binding.pwdEt.text.toString() == "" || binding.pwdCkEt.text.toString() == ""){
                //비밀번호 확인이 잘못됨
                Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }else if(binding.emailIdEt.text.toString() == "" || binding.emailComEt.text.toString() == ""){
                //이메일 입력 안했음.
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else if(!firstAgree || !secondAgree){
                Toast.makeText(this, "필수 동의사항에 동의하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
            }else{
                val PWD = binding.pwdEt.text.toString()
                val Email = binding.emailIdEt.text.toString() + binding.emailComEt.text.toString()

                auth.createUserWithEmailAndPassword(Email, PWD)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            user!!.updateEmail(Email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "User email address updated.")
                                        val UID = user.uid
                                        val intent = Intent(this, NumAuth::class.java)
                                        intent.putExtra("UID", UID)
                                        startActivity(intent)
                                    }
                                }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            binding.emailCkTxt.text = "이미 가입된 이메일입니다."
                            Toast.makeText(this, "가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.back.setOnClickListener{
            onBackPressed()
        }
    }
}