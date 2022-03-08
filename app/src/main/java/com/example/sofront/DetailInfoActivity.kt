package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.sofront.databinding.ActivityDetailInfoBinding

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
        val UName = intent.getStringExtra("UName")

        Log.d("UID : ", UID.toString())
        Log.d("UID : ", UName.toString())

        setList()
        adapter.toggleList = toggleList
        initView()
        setToggles()


        binding.userNameEt
        binding.endBt.setOnClickListener {
            //조건 수정 필요
            //이름 조건
            //모든 정보가 입력 되었는지 확인
            userInfo = adapter.userInfo

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

        return when {
            userInfo.user_name == "" -> {
                println("username!!!")
                false
            }
            userInfo.user_age == "" -> {
                println("userage!!!!")
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