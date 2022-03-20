package com.example.sofront

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sofront.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        val user = Firebase.auth.currentUser!!

        binding.profileImage.setOnClickListener{

        }

        binding.signOutBtn.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            Toast.makeText(requireContext(), "다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.changePwdBtn.setOnClickListener{
            val email = user.email.toString()
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "설정된 이메일로 비밀번호 변경 메일이 전송되었어요.", Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.resignBtn.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("짐꾼을 탈퇴하시겠습니까?")
                .setMessage("삭제된 계정은 복구되지 않습니다.\n신중하게 결정해주세요.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        user.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(requireContext(), MainActivity::class.java)
                                    Toast.makeText(requireContext(), "언제든 다시 돌아와주세요🙂", Toast.LENGTH_SHORT).show()
                                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                }
                            }
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(requireContext(), "우리 조금 더 오래 봐요😍", Toast.LENGTH_SHORT).show()
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
        return binding.root
    }
}