package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.sofront.databinding.ActivityDetailInfoBinding
import java.util.regex.Pattern

class DetailInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailInfoBinding
    private lateinit var toggleList: ArrayList<ToggleButton>
    private lateinit var llList: ArrayList<RecyclerView>
    private lateinit var textArray: ArrayList<Array<String>>
    private var userInfo: UserInfo = UserInfo()
    val adapter = SelectItemAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val UID = intent.getStringExtra("UID")

        Log.d("UID : ", UID.toString())

        setList()
        adapter.toggleList = toggleList
        initView()
        setToggles()

        binding.endBt.setOnClickListener {
            //조건 수정 필요
            //이름 조건
            //모든 정보가 입력 되었는지 확인
            userInfo = adapter.userInfo

            val idPattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{3,15}\$"
            val pattern = Pattern.compile(idPattern)


            if(binding.userNameEt.text.toString()!="") {
                val matcher = pattern.matcher(binding.userNameEt.text.toString())
                if(matcher.find())
                userInfo.user_name = binding.userNameEt.text.toString()
                else{
                    userInfo.user_name = ""
                }
            }

            if(binding.userAgeEt.text.toString()!="") {
                userInfo.user_age = binding.userAgeEt.text.toString()
            }
            else{
                userInfo.user_age = ""
            }
            if(check()) {
                RetrofitService._postUserInfo(userInfo)
            }
            else{
                println("hi")
            }
            println(userInfo.toString())
        }
    }
    private fun initView(){
        for(i in 0 until textArray.size){
            adapter.count = i
            adapter.textList = textArray[i].toMutableList()

//            adapter.buttonList[0]
            llList[i].adapter = adapter.newInstance()
            llList[i].layoutManager = GridLayoutManager(this, 3)
        }
    }
    private fun setList(){
        textArray = ArrayList()
        textArray.add(resources.getStringArray(R.array.level_array))
        textArray.add(resources.getStringArray(R.array.purpose_array))
        textArray.add(resources.getStringArray(R.array.type_array))
        textArray.add(resources.getStringArray(R.array.time_array))
        textArray.add(resources.getStringArray(R.array.number_array))

        toggleList = ArrayList()
        llList = ArrayList()

        toggleList.add(binding.userLevelToggle)
        toggleList.add(binding.userPurposeToggle)
        toggleList.add(binding.userTypeToggle)
        toggleList.add(binding.userTimeToggle)
        toggleList.add(binding.userNumberToggle)

        llList.add(binding.userLevelLl)
        llList.add(binding.userPurposeLl)
        llList.add(binding.userTypeLl)
        llList.add(binding.userTimeLl)
        llList.add(binding.userNumberLl)
    }


    private fun setToggles(){
        for(i in 0 until toggleList.size){
            toggleList[i].setOnCheckedChangeListener { _, b ->
                if(b){
                    llList[i].visibility = View.VISIBLE
                    toggleList[i].background = AppCompatResources.getDrawable(this, R.drawable.bread)
                    binding.scroll.post { binding.scroll.scrollTo(0,126) }
                }else{
                    toggleList[i].background = AppCompatResources.getDrawable(this ,R.drawable.hamburger)
                    llList[i].visibility = View.GONE
                }
            }
        }
    }


    private fun check(): Boolean {

        when {
            userInfo.user_name == "" -> {
                Toast.makeText(this,"이름을 입력해 주세요",Toast.LENGTH_SHORT)
                return false
            }
            userInfo.user_age == "" -> {
                Toast.makeText(this,"나이를 입력해 주세요",Toast.LENGTH_SHORT)
                return false
            }
            userInfo.user_level == "" ->{
                Toast.makeText(this,"운동수준을 체크해 주세요",Toast.LENGTH_SHORT)
                return false}
            userInfo.user_purpose == "" -> {
                Toast.makeText(this,"운동 목적을 체크해 주세요",Toast.LENGTH_SHORT)
                return false
            }
            userInfo.user_type == "" -> {
                Toast.makeText(this,"선호하는 운동 형태를 체크해 주세요",Toast.LENGTH_SHORT)
                return false
            }
            userInfo.user_time == "" -> {
                Toast.makeText(this,"선호하는 운동 시간을 체크해 주세요",Toast.LENGTH_SHORT)
                return false
            }
            userInfo.user_number=="" -> {
                Toast.makeText(this,"선호하는 운동 횟수를 선택해 주세요",Toast.LENGTH_SHORT)
                return false
            }
            else -> {
                println("입력성공")
                return true
            }
        }
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
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}