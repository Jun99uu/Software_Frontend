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
    @POST("userInfo/")
    fun postUserInfo(@Body userInfo: UserInfo) : Call<UserInfo>

    @POST("welcome/")
    fun postUID(@Body UID: UID): Call<UID>

    @POST("auth_check/")
    fun postAuth(@Body UID: UID): Call<UID>

    companion object{
        private const val BASE_URL = "http://a041-219-255-158-173.ngrok.io/database/"

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
            var successful:Boolean = false
            retrofitService.postUID(UID).enqueue(object: Callback<UID>{
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

        ///////인증 성공 후 전송
        fun _postAuth(UID: UID):Boolean{
            var successful:Boolean = false
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

    }
}