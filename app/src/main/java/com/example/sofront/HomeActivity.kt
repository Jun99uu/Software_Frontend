package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.sofront.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomBar.setItemSelected(R.id.daily)
        openHomeFragment()

        binding.bottomBar.setOnItemSelectedListener {
            when(it){
                R.id.daily -> {
                    openHomeFragment()
                }
                R.id.calendar -> {
                    val listFragment = ListFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, listFragment).commit()
                }
                R.id.plan -> {
                    val planFragment = PlanCollectionFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, planFragment).commit()
                }
                R.id.subscribe -> {
                    val heartFragment = HeartFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, heartFragment).commit()
                }
                R.id.profile -> {
                    val profileFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, profileFragment).commit()
                }
            }
        }
    }

    private fun openHomeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, HomeFragment())
        transaction.commit()
    }

    fun replaceFragment(fragment:Fragment, value:Int){
        if(value == 0){
            binding.bottomBar.setItemSelected(R.id.calendar,true)
        }else if(value == 1){
            binding.bottomBar.setItemSelected(R.id.plan, true)
        }
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 1500) {
//            ActivityCompat.finishAffinity(this); // 액티비티를 종료하고
//            System.exit(0); // 프로세스를 종료
            moveTaskToBack(true);						// 태스크를 백그라운드로 이동
            finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료
            return
        }

        // 처음 클릭 메시지
        Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}