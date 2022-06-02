package com.example.sofront

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.room.RoomDatabase
import com.example.sofront.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.FirebaseAuthKtxRegistrar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeActivity : AppCompatActivity() {
    lateinit var binding:ActivityHomeBinding
    val user = Firebase.auth.currentUser

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openHomeFragment() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        val formatted = current.format(formatter)
        val transaction = supportFragmentManager.beginTransaction()
        val calendarInstance = CalendarDatabase.getInstance(this)
        val calendarDao = calendarInstance.calendarDao()
        CoroutineScope(Dispatchers.IO).launch {
            val planEntity = calendarDao.getPlanByDay(formatted)
            if(planEntity!=null){
                startTodayRoutine(planEntity.planName, planEntity.count)
            }else{
                transaction.replace(R.id.frame_layout, HomeFragment())
            }
            transaction.commit()
        }
    }
    fun startTodayRoutine(planName : String, count : Int){
        RetrofitService.retrofitService.getPlanByPlanName(planName, FirebaseAuth.getInstance().uid.toString()).enqueue(object : Callback<Plan>{
            override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                if(response.isSuccessful){
                    Log.d("홈프래그먼트에서 오늘 루틴 가져오기 통신 성공",response.body().toString())

                    val f = DailyRoutineFragment()
                    val bundle = Bundle()
                    bundle.putSerializable("routine",response.body()!!.routineList[count])
                    f.arguments = bundle
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, f).commit()
                }
                else{
                    Log.e("홈프래그먼트에서 오늘 루틴 가져오기 통신 약간 에러",response.code().toString())
                }
            }

            override fun onFailure(call: Call<Plan>, t: Throwable) {
                Log.d("홈프래그먼트에서 오늘 루틴 가져오기 통신 완전 에러",t.message!!)
            }

        })
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
            moveTaskToBack(true)						// 태스크를 백그라운드로 이동
            finishAndRemoveTask()					// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid())	// 앱 프로세스 종료
            return
        }

        // 처음 클릭 메시지
        Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}