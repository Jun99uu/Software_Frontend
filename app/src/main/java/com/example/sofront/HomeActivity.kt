package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.sofront.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomBar.setItemSelected(R.id.home)
        openHomeFragment()

        binding.bottomBar.setOnItemSelectedListener {
            when(it){
                R.id.home -> {
                    openHomeFragment()
                }
                R.id.list -> {
                    val listFragment = ListFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, listFragment).commit()
                }
                R.id.heart -> {
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

    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 1500) {
            ActivityCompat.finishAffinity(this); // 액티비티를 종료하고
            System.exit(0); // 프로세스를 종료
            return
        }

        // 처음 클릭 메시지
        Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}