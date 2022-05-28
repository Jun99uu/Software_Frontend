package com.example.sofront

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sofront.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pwdEt.setOnEditorActionListener{ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                // 키보드 내리기
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.pwdEt.windowToken, 0)
                handled = true
            }
            handled
        }

        val mailArray: Array<String> = arrayOf("@naver.com", "@gmail.com", "@hanmail.net", "@nate.com")
        var mailcom:String
        binding.emailComEt.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("이메일 선택")
                .setItems(mailArray,
                    DialogInterface.OnClickListener { dialog, which ->
                        // 여기서 인자 'which'는 배열의 position을 나타냅니다.
                        binding.emailComEt.text = mailArray[which]
//                        mailcom = mailArray[which]
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }

        auth = Firebase.auth

        binding.regiBtn.setOnClickListener{
            val intent = Intent(this, SignUpAuth::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener{
            val email = binding.emailIdEt.text.toString() + binding.emailComEt.text.toString()
            val password = binding.pwdEt.text.toString()

            if(binding.emailIdEt.text.toString() == "" || binding.emailComEt.text.toString() == ""){
                Toast.makeText(baseContext, "이메일을 입력해주세요🤔", Toast.LENGTH_SHORT).show()
            }else if(binding.pwdEt.text.toString() == ""){
                Toast.makeText(baseContext, "비밀번호를 입력해주세요🤔", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
//                            RetrofitService._login(auth.uid.toString())
                            RetrofitService.retrofitService.login(UID(auth.uid.toString())).enqueue(object :
                                Callback<UID> {
                                override fun onResponse(call: Call<UID>, response: Response<UID>) {
                                    if(response.isSuccessful){
                                        Log.d("login","success")
                                        Log.d("login code ",response.code().toString())
                                        Log.d("login body", response.body().toString())
                                        Log.d(TAG, "signInWithEmail:success")
                                        val user = auth.currentUser
                                        updateUI(user)
                                    }
                                    else{
                                        //TODO("로그인 실패한 이유를 받아서 추가정보를 받는 등의 대응을 해줘야함")
                                        if(response.message().equals("Unauthorized")){
                                            //TODO("번호인증 안한놈")
                                        }
                                        Log.e("login","success but something error")
                                        Log.e("login error code",response.code().toString())
                                        Log.e("login error message",response.message())
                                        Log.e("login error errorbody",response.errorBody()!!.string())
                                        Log.e("login error body",response.body().toString())
                                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                                        Toast.makeText(baseContext, "로그인에 실패했습니다😢",
                                            Toast.LENGTH_SHORT).show()
                                        updateUI(null)
                                    }
                                }

                                override fun onFailure(call: Call<UID>, t: Throwable) {
                                    Log.e("login","fail")
                                    Log.e("login error message",t.message.toString())
                                    Toast.makeText(baseContext, "로그인에 실패했습니다😢",
                                        Toast.LENGTH_SHORT).show()
                                    updateUI(null)
                                }

                            })
//                            Log.d(TAG, "signInWithEmail:success")
//                            val user = auth.currentUser
//                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "로그인에 실패했습니다😢\n아이디와 비밀번호를 확인해 주세요",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?){
        if(user != null){
            val intent = Intent(this, HomeActivity::class.java)
            Toast.makeText(baseContext, "환영합니다😊",
                Toast.LENGTH_SHORT).show()
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            this.finish()
        }
    }


}