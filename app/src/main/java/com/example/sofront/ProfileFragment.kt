package com.example.sofront

import android.content.ComponentCallbacks
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.sofront.RetrofitService.Companion.retrofitService
import com.example.sofront.databinding.ActivityProfileBinding
import com.example.sofront.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private var mBinding: ActivityProfileBinding? = null
    private val binding get() = mBinding!!

    val user = Firebase.auth.currentUser
    val myUid = user?.uid.toString()
    val defaultImg = R.drawable.gymdori
    val defaultBack = R.drawable.womanrun
    lateinit var profile:Profile

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = ActivityProfileBinding.inflate(layoutInflater)

        _getProfile(myUid)
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

        binding.suboreditBtn.text = "편집"
        binding.suboreditBtn.setOnClickListener{
            val intent = Intent(context, EditProfileActivity::class.java)
            //프로필사진, 배경사진(url) _ 보고있는 프로필의 uid, 이름, 소개글(String)으로 넘겨줘야함.
            intent.putExtra("nickname", "임시용")
            intent.putExtra("subtitle", "하이 방가방가데스요")
            startActivity(intent)
        }

        val fragmentList = listOf(ProfilePortfolioFragment(), ProfilePlanFragment())
        val adapter = ProfileVPAdapter(requireActivity())
        adapter.fragments = fragmentList
        binding.profileMainSheet.adapter = adapter

        binding.portfolioBtn.setOnClickListener{
            binding.profileMainSheet.setCurrentItem(0, true)
        }
        binding.madePlanBtn.setOnClickListener{
            binding.profileMainSheet.setCurrentItem(1, true)
        }
        binding.makePortfolioBtn.setOnClickListener{
            val portfolioIntent = Intent(context, MakePortfolioActivity::class.java)
            startActivity(portfolioIntent)
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
        return binding.root
    }

    fun _getProfile(uid:String){
        retrofitService.getProfile(uid).enqueue(object : Callback<Profile> {
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

    fun updatePhotoUrl(url:String){
        val profileUpdates = userProfileChangeRequest {
            photoUri = Uri.parse(url)
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("photoUrl update", "User profile updated.")
                }
            }
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

        updatePhotoUrl(profile.profileImg)
    }

}