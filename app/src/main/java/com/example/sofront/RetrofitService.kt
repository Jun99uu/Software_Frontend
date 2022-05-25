package com.example.sofront

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {
    //1. 인터페이스 설계
    @POST("/info") //레거시
    fun postUserInfo(@Body userInfo: UserInfo) : Call<UserInfo>

    @POST("/account/signup") //회원가입 _ 파이어베이스 uid를 서버에 보내줌
    fun postUID(@Body UID: UID): Call<UID>

    @PUT("/account/phone") //핸드폰 번호 인증 했는지 안했는지
    fun postAuth(@Body UID: UID): Call<UID>

    @POST("/account/login") //로그인
    fun login(@Body UID: UID): Call<UID>

    @POST("/workout/planSet") //플랜 생성
    fun setPlan(@Body Plan: Plan): Call<Plan>

    @GET("/workout/planGet/{planName}") //플랜이름으로 받아오기
    fun getPlanByPlanName(@Path("planName") planName : String) : Call<Plan>

    @GET("/workout/planGet/{uid}/UID") //uid로 플랜 가져오기
    fun getPlanByUid(@Path("uid") uid:String) : Call<ArrayList<Plan>>

    @GET("/workout/plan/download/{uid}/UID")
    fun getDownloadPlanByUid(@Path("uid") uid:String) : Call<ArrayList<Plan>>

    @GET("/workout/planGetHashTag/{hashtag}")//해시태그로 플랜 가져오기
    fun getPlanByHashTag(@Path("hashtag") hashTag:String) : Call<ArrayList<Plan>>

    @GET("/profiles/get_portfolio/{uid}") //uid로 포트폴리오 가져오기
    fun getPortfolio(@Path("uid") uid:String) : Call<ArrayList<Portfolio>>

    @GET("/portfolio/subscription/{uid}") //uid로 구독 목록 가져오기
    fun getSubscribingPortfolio(@Path("uid")uid: String) : Call<ArrayList<Portfolio>>

    @POST("/plan/like") //플랜 이름으로 좋아요 보내기
    fun postLike(@Body planLike:planLike) : Call<planLike>

    //플랜 이름으로 다운로드 회원 보내기
    //플랜 이름으로 댓글 받아오기

    @GET("profiles/get_profile/{UID}") //회원 uid로 프로필 정보 받아오기
    fun getProfile(@Path("UID") UID:String) : Call<Profile>

    @POST("/profile/edit") //프로필 수정
    fun editProfile(@Body profile:Profile) : Call<Profile>

    @POST("/subscribe") //구독하기
    fun postSubscribe(@Body subscribe: subscribeProfile) : Call<subscribeProfile>

    companion object{
        //var gson = GsonBuilder().setLenient().create()
        private const val BASE_URL = "http://7bfd-219-255-158-172.ngrok.io"

        val retrofitService = create()

        //2. 인터페이스 연결
        private fun create():RetrofitService{
            val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //뭐오류나면 여기에 gson
                .build()

            return retrofit.create(RetrofitService::class.java)
        }

        //3. 인터페이스 사용
        fun _postUserInfo(userInfo: UserInfo){
            /////////////////id 가져오기
            retrofitService.postUserInfo(userInfo).enqueue(object: Callback<UserInfo> {
                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    if(response.isSuccessful){
                        response.message()
                        Log.d("Post","success $response")
                    }
                    else {
                        Log.d("Post", "success,but ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    Log.d("Post","fail $t")
                }
            })
        }

        ///////UID 전송
        fun _postUID(UID: UID):Boolean{
            var successful = false
            retrofitService.postUID(UID).enqueue(object: Callback<UID>{
                override fun onResponse(call: Call<UID>, response: Response<UID>) {
                    if(response.isSuccessful){
                        Log.d("PostUID","success $response")
                        Log.d("PostUID", response.message())
                        Log.d("PostUID",response.toString())
                        Log.d("PostUID : code",response.code().toString())
                        Log.d("PostUID : body",response.body().toString())
                        successful = true
                    }else {
                        Log.d("Post", "success,but error body : ${response.errorBody()}")
                        Log.d("Post", response.message())
                        Log.d("PostUID",response.raw().toString())
                        Log.d("response.tostring()",response.toString())
                        Log.d("body", response.body().toString())
                        Log.d("PostUID : code",response.code().toString())

                    }
                }
                override fun onFailure(call: Call<UID>, t: Throwable) {
                    Log.d("Post","fail $t")
                }
            })
            return successful
        }

        ///////인증 성공 후 전송
        fun _postAuth(UID: UID):Boolean{
            var successful = false
            retrofitService.postAuth(UID).enqueue(object: Callback<UID>{
                override fun onResponse(call: Call<UID>, response: Response<UID>) {
                    if(response.isSuccessful){
                        Log.d("Post","success $response")
                        successful = true
                    }else {
                        Log.d("Post", "success,but ${response.errorBody()}")
                    }
                }
                override fun onFailure(call: Call<UID>, t: Throwable) {
                    Log.d("Post","fail $t")
                }
            })
            return successful
        }

        fun _getPlanByUid(uid: String) : ArrayList<Plan>{
            var myPlan = ArrayList<Plan>()
            retrofitService.getPlanByUid(uid).enqueue(object : Callback<ArrayList<Plan>> {
                override fun onResponse(call: Call<ArrayList<Plan>>, response: Response<ArrayList<Plan>>) {
                    if (response.isSuccessful) {
                        Log.d("getPlan test", "success")
                        Log.d("getPlan test success", response.body().toString())
                        myPlan = response.body()!!
                    } else {
                        Log.d("getPlan test", "success but something error")
                    }
                }

                override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                    Log.d("getPlan test", "fail")
                }
            })
            return myPlan
        }

        fun _login(uid:String)  {
            retrofitService.login(UID(uid)).enqueue(object : Callback<UID>{
                override fun onResponse(call: Call<UID>, response: Response<UID>) {
                    if(response.isSuccessful){
                        Log.d("login","success")
                        Log.d("login code ",response.code().toString())
                        Log.d("login body", response.body().toString())


                    }
                    else{
                        Log.d("login","success but something error")
                    }
                }

                override fun onFailure(call: Call<UID>, t: Throwable) {
                    Log.d("login","fail")
                }

            })
        }

        fun _setPlan(plan: Plan){
            retrofitService.setPlan(plan).enqueue(object : Callback<Plan>{
                override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                    if(response.isSuccessful){
                        Log.d("setPlan test","success")
                    }
                    else{
                        Log.d("setPlan test","success but something error"+response.code())
                    }
                }

                override fun onFailure(call: Call<Plan>, t: Throwable) {
                    Log.d("setPlan test","fail")
                }
            })
        }

        fun _getPlanByPlanName(planName:String) : Plan {
            var myPlan = Plan("", ArrayList(), ArrayList(), "", true, 0, 0, 0)
            retrofitService.getPlanByPlanName(planName).enqueue(object : Callback<Plan> {
                override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                    if (response.isSuccessful) {
                        Log.d("getPlan test", "success")
                        Log.d("getPlan test success", response.body().toString())
                        myPlan = response.body()!!
                    } else {
                        Log.d("getPlan test", "success but something error")
                    }
                }

                override fun onFailure(call: Call<Plan>, t: Throwable) {
                    Log.d("getPlan test", "fail")
                }
            })
            return myPlan
        }
        fun _getDownloadPlanByUid(uid:String):ArrayList<Plan>{
            var myPlan = ArrayList<Plan>()
            retrofitService.getDownloadPlanByUid(uid).enqueue(object : Callback<ArrayList<Plan>> {
                override fun onResponse(call: Call<ArrayList<Plan>>, response: Response<ArrayList<Plan>>) {
                    if (response.isSuccessful) {
                        Log.d("getDownLoadPlan test success", response.body().toString())
                        Log.d("getDownLoadPlan test success", response.body()!!.size.toString())
                        myPlan = response.body()!!
                    } else {
                        Log.d("getDownLoadPlan test", "success but something error")
                    }
                }

                override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                    Log.d("getDownLoadPlan test", "fail")
                }
            })
            return myPlan
        }
        fun _getPlanByHashTag(hashTag: String) : ArrayList<Plan>{
            var myPlan = ArrayList<Plan>()
            retrofitService.getPlanByUid(hashTag).enqueue(object : Callback<ArrayList<Plan>> {
                override fun onResponse(call: Call<ArrayList<Plan>>, response: Response<ArrayList<Plan>>) {
                    if (response.isSuccessful) {
                        Log.d("getPlan test success", response.body().toString())
                        myPlan = response.body()!!
                    } else {
                        Log.d("getPlan test", "success but something error")
                    }
                }

                override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                    Log.d("getPlan test", "fail")
                }
            })
            return myPlan
        }

//        fun _getPortfolio(uid:String) : ArrayList<Portfolio>{
//            var myPortfolio = ArrayList<Portfolio>()
//            retrofitService.getPortfolio(uid).enqueue(object  :Callback<ArrayList<Portfolio>>{
//                override fun onResponse(
//                    call: Call<ArrayList<Portfolio>>,
//                    response: Response<ArrayList<Portfolio>>
//                ) {
//                    if (response.isSuccessful) {
//                        Log.d("getPortfolio test success", response.body().toString())
//                        myPortfolio = response.body()!!
//                    } else {
//                        Log.d("getPortfolio test", "success but something error")
//                    }
//                }
//
//                override fun onFailure(call: Call<ArrayList<Portfolio>>, t: Throwable) {
//                    Log.d("getPortfolio test", "fail")
//                }
//
//            })
//            return myPortfolio
//        }

        fun _getSubscribingPortfolio(uid:String) : ArrayList<Portfolio>{
            var myPortfolio = ArrayList<Portfolio>()
            retrofitService.getSubscribingPortfolio(uid).enqueue(object  :Callback<ArrayList<Portfolio>>{
                override fun onResponse(
                    call: Call<ArrayList<Portfolio>>,
                    response: Response<ArrayList<Portfolio>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("getPortfolio test success", response.body().toString())
                        myPortfolio = response.body()!!
                    } else {
                        Log.d("getPortfolio test", "success but something error")
                    }
                }

                override fun onFailure(call: Call<ArrayList<Portfolio>>, t: Throwable) {
                    Log.d("getPortfolio test", "fail")
                }

            })
            return myPortfolio
        }

//        fun _getProfile(uid:String) : Profile{
//            var profile = Profile(uid, "", "", "", "", 0)
//            retrofitService.getProfile(uid).enqueue(object :Callback<Profile>{
//                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
//                    if(response.isSuccessful){
//                        Log.d("getProfile test success", response.body().toString())
//                        profile = response.body()!!
//                    }else{
//                        Log.d("getProfile test", "success but something error")
//                    }
//                }
//                override fun onFailure(call: Call<Profile>, t: Throwable) {
//                    Log.d("getProfile test", "fail")
//                }
//            })
//            return profile
//        }

        fun _editProfile(profile:Profile){
            retrofitService.editProfile(profile).enqueue(object :Callback<Profile>{
                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                    if(response.isSuccessful){
                        Log.d("editProfile test success", response.body().toString())
                    }else{
                        Log.d("editProfile test", "success but something error")
                    }
                }
                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    Log.d("editPortfolio test", "fail")
                }

            })
        }
    }
}