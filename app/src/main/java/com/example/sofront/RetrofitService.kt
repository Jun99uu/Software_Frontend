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
    @POST("user_info/")
    fun postUserInfo(@Body userInfo: UserInfo) : Call<UserInfo>

    @GET("plan/{uid}")
    fun getPlan(@Path("uid") uid : String) : Call<Plan>


    companion object{
        private const val BASE_URL = "http://2070-219-255-158-172.ngrok.io/database/"

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
                        Log.d("Post","success $response")
                    }
                    else {
                        Log.d("Post", "success,but ${response.errorBody()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    Log.d("Post","fail $t")
                }
            })
        }

//        fun _getPlan(uid: String) : Plan{
//            var myPlan: Plan
//
//            retrofitService.getPlan(uid).enqueue(object : Callback<Plan>{
//                override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
//                    if(response.isSuccessful){
//                        if(response.body()!=null){
//                            myPlan = (response.body()) as Plan
//                        }
//                    }
//                    else{
//
//                    }
//                }
//
//                override fun onFailure(call: Call<Plan>, t: Throwable) {
//
//                }
//            })
//            return myPlan
//        }


    }
}