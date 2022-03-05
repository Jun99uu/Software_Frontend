package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ToggleButton

import com.example.sofront.databinding.ActivityDetailInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailInfoActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var binding: ActivityDetailInfoBinding
    private lateinit var toggleList: ArrayList<ToggleButton>
    private lateinit var llList: ArrayList<LinearLayout>
    private var userInfo: UserInfo = UserInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val UID = intent.getStringExtra("UID")
        val UName = intent.getStringExtra("UName")

        Log.d("UID : ", UID.toString())
        Log.d("UID : ", UName.toString())
        setToggles()
        setButtons()

        binding.userNameEt
        binding.endBt.setOnClickListener {
            //조건 수정 필요
            //이름 조건
            //모든 정보가 입력 되었는지 확인

            userInfo.user_name = binding.userNameEt.text.toString()
            if(binding.userAgeEt.text.toString()!="")
            userInfo.user_age = binding.userAgeEt.text.toString()

            if(check()) {
                println(userInfo)
                RetrofitService._postUserInfo(userInfo)
            }
            else{
                println("입력정보확인")
            }
        }
    }

    private fun setToggles(){
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

        for(i in 0 until toggleList.size){
            toggleList[i].setOnCheckedChangeListener { _, b ->
                if(b){
                    llList[i].visibility = View.VISIBLE
                    binding.scroll.post { binding.scroll.scrollTo(0,126) }
                }else{
                    llList[i].visibility = View.GONE
                }
            }
        }
    }
    override fun onClick(v: View?) {
        val button = v as Button
        val id = v.id
        if(id == binding.hellEmbryoBt.id || id == binding.hellChangBt.id || id==binding.hellBabyBt.id ||id==binding.hellTeenBt.id){
            expand(0)
            binding.levelSelectedTv.text = button.text.toString()
            userInfo.user_level = button.text.toString()
        }
        else if(id == binding.purposeHealthBt.id || id ==binding.purposeBulkupBt.id || id==binding.purposeDietBt.id){
            expand(1)
            binding.purposeSelectedTv.text=button.text.toString()
            userInfo.user_purpose = button.text.toString()
        }
        else if(id == binding.typeCardioBt.id || id==binding.typeWeightBt.id||id==binding.typeXxxBt.id){
            expand(2)
            binding.typeSelectedTv.text=button.text.toString()
            userInfo.user_type = button.text.toString()
        }
        else if(id == binding.time1h30mBt.id||id==binding.time1hBt.id||id==binding.time30mBt.id){
            expand(3)
            binding.timeSelectedTv.text=button.text.toString()
            userInfo.user_time = button.text.toString()
        }
        else{
            expand(4)
            binding.numberSelectedTv.text=button.text.toString()
            userInfo.user_number = button.text.toString()
        }
    }
    private fun expand(x: Int){
        for (i in 0 until toggleList.size) {
            val toggleButton = toggleList[i]
            if(i==x && toggleButton.isChecked) {
                toggleButton.toggle() }
            else if(i==x+1 && x < toggleList.size ){
                if(!toggleButton.isChecked) toggleButton.toggle()
            }
            else{
                if(toggleButton.isChecked) {
                    toggleButton.toggle()
                }
            }
        }
    }

    private fun setButtons(){
        binding.hellEmbryoBt.setOnClickListener(this)
        binding.hellBabyBt.setOnClickListener(this)
        binding.hellTeenBt.setOnClickListener(this)
        binding.hellChangBt.setOnClickListener(this)

        binding.purposeBulkupBt.setOnClickListener(this)
        binding.purposeDietBt.setOnClickListener(this)
        binding.purposeHealthBt.setOnClickListener(this)

        binding.typeCardioBt.setOnClickListener(this)
        binding.typeWeightBt.setOnClickListener(this)
        binding.typeXxxBt.setOnClickListener(this)

        binding.time1h30mBt.setOnClickListener(this)
        binding.time1hBt.setOnClickListener(this)
        binding.time30mBt.setOnClickListener(this)

        binding.number1Bt.setOnClickListener(this)
        binding.number2Bt.setOnClickListener(this)
        binding.number3Bt.setOnClickListener(this)
        binding.number4Bt.setOnClickListener(this)
        binding.number5Bt.setOnClickListener(this)
        binding.number6Bt.setOnClickListener(this)
        binding.number7Bt.setOnClickListener(this)

    }
    private fun check(): Boolean {

        return when {
            userInfo.user_name == "" -> {
                println("username!!!")
                false
            }
            userInfo.user_age == "" -> {
                println("userageg!!!!")
                false
            }
            userInfo.user_level == "" -> false
            userInfo.user_purpose == "" -> false
            userInfo.user_type == "" -> false
            userInfo.user_time == "" -> false
            userInfo.user_number=="" -> false
            else -> true
        }
    }
}