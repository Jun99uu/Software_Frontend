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
    @POST("users/{id}/info")
    fun postUserInfo(@Path("id")id:String, @Body userInfo: UserInfo) : Call<UserInfo>



    companion object{
        private const val BASE_URL = "http://127.0.0.1:8000/"

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
            retrofitService.postUserInfo("123",userInfo).enqueue(object: Callback<UserInfo> {
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


    }
}