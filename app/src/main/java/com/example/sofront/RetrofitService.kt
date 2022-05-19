package com.example.sofront

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {
    //1. 인터페이스 설계
    @POST("/info")
    fun postUserInfo(@Body userInfo: UserInfo) : Call<UserInfo>

    @POST("/account/signup")
    fun postUID(@Body UID: UID): Call<UID>

    @PUT("/account/phone")
    fun postAuth(@Body UID: UID): Call<UID>

    @POST("/account/login")
    fun login(@Body UID: UID): Call<UID>

    @POST("/workout/planSet")
    fun setPlan(@Body Plan: Plan): Call<Plan>

    @GET("/workout/planGet/{planName}")
    fun getPlanByPlanName(@Path("planName") planName : String) : Call<Plan>
    @GET("/workout/planGetUID/{uid}")
    fun getPlanByUid(@Path("uid") uid:String) : Call<ArrayList<Plan>>
    @GET("/workout/planGetHashTag/{hashtag}")
    fun getPlanByHashTag(@Path("hashtag") hashTag:String) : Call<ArrayList<Plan>>

    companion object{
        private const val BASE_URL = "http://ef92-49-142-63-121.ngrok.io"

        val retrofitService = create()

        //2. 인터페이스 연결
        private fun create():RetrofitService{
            val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
        suspend fun _getPlanByUid(uid: String) : ArrayList<Plan>{
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
        fun _getPlanByHashTag(hashTag: String) : ArrayList<Plan>{
            var myPlan = ArrayList<Plan>()
            retrofitService.getPlanByUid(hashTag).enqueue(object : Callback<ArrayList<Plan>> {
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

    }
}