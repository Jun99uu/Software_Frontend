package com.example.sofront

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sofront.databinding.ActivityDailyRoutinePlayBinding

class DailyRoutinePlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDailyRoutinePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workoutName = binding.workoutName //운동이름
        val workoutBar = binding.workoutBar //진행바
        val playBtn = binding.playBtn //세트진행 버튼
        val nextBtn = binding.nextBtn //다음세트 버튼
        val help = binding.help //응원문구 (세트끝나면 잘했다고 바꿔주기)


    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("플랜헬퍼")
            .setMessage("정말로 운동을 끝내시겠습니까?.\n현재 운동 횟수는 기록되지 않습니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //확인클릭
                    super.onBackPressed()
                    Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //취소클릭
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }
}