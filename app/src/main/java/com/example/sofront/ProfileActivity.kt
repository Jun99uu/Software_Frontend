package com.example.sofront

import android.content.Intent
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.sofront.databinding.ActivityProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private var mBinding: ActivityProfileBinding? = null
    private val binding get() = mBinding!!
    val user = Firebase.auth.currentUser
    val myUid = user?.uid.toString()
    lateinit var thisUid:String
    val defaultImg = R.drawable.gymdori
    val defaultBack = R.drawable.womanrun
    lateinit var profile:Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thisUid = intent.getStringExtra("UID").toString()
        _getProfile(thisUid)

        binding.profileImg.translationZ = 1f

        val prevPadding:Int = Math.round(resources.displayMetrics.density * 30) //30dp 변환값

        binding.profileScrollView.run {
            header = binding.header
            stickListener = { _ ->
                Log.d("스틱헤더 : ", "붙었다")
                binding.portfolioBtn.setPadding(0,0,0,0)
                binding.madePlanBtn.setPadding(0,0,0,0)
            }
            freeListener = { _ ->
                Log.d("스틱헤더 : ", "떨어졌다")
                binding.portfolioBtn.setPadding(0,0,prevPadding,0)
                binding.madePlanBtn.setPadding(prevPadding,0,0,0)
            }
        }

        if(thisUid.equals(myUid)){
            //현재 uid와 들어온 프로필의 uid가 같다면
            binding.suboreditBtn.text = "편집"
        }

        binding.suboreditBtn.setOnClickListener{
            if(true){
                val intent = Intent(this, EditProfileActivity::class.java)
                //프로필사진, 배경사진(비트맵) _ 보고있는 프로필의 uid, 이름, 소개글(String)으로 넘겨줘야함.
                intent.putExtra("nickname", profile.name)
                intent.putExtra("subtitle", profile.subTitle)
                intent.putExtra("profileImg", profile.profileImg)
                intent.putExtra("background", profile.backgroundImg)
                startActivity(intent)
            }else{
                //구독
            }
        }

        val fragmentList = listOf(ProfilePortfolioFragment(), ProfilePlanFragment())
        val adapter = ProfileVPAdapter(this)
        adapter.fragments = fragmentList
        binding.profileMainSheet.adapter = adapter

        binding.portfolioBtn.setOnClickListener{
            binding.profileMainSheet.setCurrentItem(0, true)
        }
        binding.madePlanBtn.setOnClickListener{
            binding.profileMainSheet.setCurrentItem(1, true)
        }

        binding.profileMainSheet.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val view = (binding.profileMainSheet[0] as RecyclerView).layoutManager?.findViewByPosition(position)
                view?.post {
                    val wMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)
                    if (binding.profileMainSheet.layoutParams.height != view.measuredHeight) {
                        binding.profileMainSheet.layoutParams = (binding.profileMainSheet.layoutParams).also { lp ->
                            lp.height = view.measuredHeight
                        }
                    }
                }
                if(position == 0){
                    val pfShape : GradientDrawable = binding.portfolioBtn.background as GradientDrawable
                    pfShape.setColor(Color.parseColor("#61A4BC"))
                    val plShape : GradientDrawable = binding.madePlanBtn.background as GradientDrawable
                    plShape.setColor(Color.parseColor("#3F3F3F"))
                }else{
                    val plShape : GradientDrawable = binding.madePlanBtn.background as GradientDrawable
                    plShape.setColor(Color.parseColor("#61A4BC"))
                    val pfShape : GradientDrawable = binding.portfolioBtn.background as GradientDrawable
                    pfShape.setColor(Color.parseColor("#3F3F3F"))
                }
            }
        })
    }

    fun _getProfile(uid:String){
        RetrofitService.retrofitService.getProfile(uid).enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if(response.isSuccessful){
                    Log.d("getProfile test success", response.body().toString())
                    profile = response.body()!!
                    refreshProfile()
                }else{
                    Log.d("getProfile test", "success but something error")
                }
            }
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Log.d("getProfile test", "fail")
            }
        })
    }

    fun refreshProfile(){
        binding.userName.text = profile.name
        binding.subscribeNum.text = "Sub. ${profile.subscribeNum}명"
        binding.profileContent.text = profile.subTitle
        Glide.with(this)
            .load(profile.profileImg) // 불러올 이미지 url
            .placeholder(defaultImg) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImg) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImg) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(binding.profileImg) // 이미지를 넣을 뷰

        Glide.with(this)
            .load(profile.backgroundImg) // 불러올 이미지 url
            .placeholder(defaultBack) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultBack) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultBack) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(binding.backgroundImage) // 이미지를 넣을 뷰
    }

}