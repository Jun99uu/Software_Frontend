package com.example.sofront

import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {
    //1. 인터페이스 설계
    @POST("/accounts/info") //레거시
    fun postUserInfo(@Body userInfo: UserInfo) : Call<UserInfo>

    @POST("/accounts/signup") //회원가입 _ 파이어베이스 uid를 서버에 보내줌
    fun postUID(@Body UID: UID): Call<UID>

    @POST("/accounts/phone") //핸드폰 번호 인증 했는지 안했는지
    fun postAuth(@Body UID: UID): Call<UID>

    @POST("/accounts/login") //로그인
    fun login(@Body UID: UID): Call<UID>

    @POST("/workout/planSet") //플랜 생성
    fun setPlan(@Body Plan: Plan): Call<Plan>

    @GET("/workout/planGet/{planName}/plan") //플랜이름으로 받아오기
    fun getPlanByPlanName(@Path("planName") planName : String) : Call<Plan>

    @GET("/workout/planGet/{uid}/UID") //uid로 플랜 가져오기
    fun getPlanByUid(@Path("uid") uid:String) : Call<ArrayList<Plan>>

    @GET("/workout/plan/my/{UID}") //내 프로필 안에서 플랜 받아오기
    fun getMyPlanInProfile(@Path("UID") UID:String) : Call<ArrayList<Plan>>

    @GET("/workout/plan/all/{uid}")
    fun getDownloadPlan(@Path("uid") uid : String) : Call<ArrayList<Plan>>

    @GET("/workout/plan/share/")//해시태그로 플랜 가져오기
    fun getPlanByHashTag() : Call<ArrayList<ArrayList<summaryPlan>>>

    @GET("/profiles/get_portfolio/{uid}") //uid로 포트폴리오 가져오기
    fun getPortfolio(@Path("uid") uid:String) : Call<ArrayList<Portfolio>>

    @GET("/profiles/subscribe/portfolio/{uid}") //uid로 구독 목록 가져오기
    fun getSubscribingPortfolio(@Path("uid")uid: String) : Call<ArrayList<Portfolio>>

    @POST("/plan/like") //플랜 이름으로 좋아요 보내기
    fun postLike(@Body planLike:planLike) : Call<planLike>

    @POST("/workout/plan/comment") //플랜에서 댓글 작성
    fun postCommentInPlan(@Body comment:planComment) : Call<planComment>

    @GET("/workout/plan/comment/{planName}") //플랜 이름으로 댓글 받아오기
    fun getCommentByPlanName(@Path("planName")planName: String) : Call<ArrayList<Comment>>

    @GET("/profiles/get_profile/{UID}/{myUID}") //회원 uid로 프로필 정보 받아오기
    fun getProfile(@Path("UID") UID:String,@Path("myUID") myUID:String) : Call<Profile>

    @PUT("/profiles/modify_profile/{UID}") //프로필 수정
    fun editProfile(@Path("UID") UID:String, @Body editProfile:editProfile) : Call<editProfile>

    @POST("/profiles/subscribe") //구독하기
    fun postSubscribe(@Body subscribe: subscribeProfile) : Call<subscribeProfile>

    @GET("/profiles/get_comments/{portfolioID}")
    fun getPortfolioComment(@Path("portfolioID") porfolioID:String) : Call<ArrayList<Comment>>

    @POST("/profiles/post_comments/")
    fun postPortfolioComment(@Body comment: Comment) : Call<Comment>

    @POST("/profiles/makes_portfolios/")
    fun postPortfolio(@Body sendPortfolio: SendPortfolio) : Call<SendPortfolio>
    @DELETE("/profiles/delete_comments/{commentID}")
    fun deletePortfolioComment(@Path("commentID") commentID : String) : Call<Comment>
    @DELETE("/plan/comment/del/{commentN}")
    fun deletePlanComment(@Path("commentN") commmentN : String) : Call<Comment>
    @POST("/profiles/portfolio_like")
    fun postPortfolioLike(@Body like : PortfolioLike) : Call<PortfolioLike>
    fun deleteComment(@Path("commentID") commentID : String) : Call<Comment>

    @GET("/profiles/img/name/{UID}")
    fun getCertainProfile(@Path("UID") UID: String) : Call<certainProfile>

    companion object{
        //var gson = GsonBuilder().setLenient().create()
        private const val BASE_URL = "http://5f82-219-255-158-172.ngrok.io"

        val retrofitService = create()

        //2. 인터페이스 연결
        private fun create():RetrofitService{
            val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //뭐오류나면 여기에 gson
                .build()

            return retrofit.create(RetrofitService::class.java)
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
                        Log.d("PostAuth","success $response")
                        successful = true
                    }else {
                        Log.e("PostAuth ", "success,but something error")
                        Log.e("PostAuth error code",response.code().toString())
                        Log.e("PostAuth error message",response.message())
                    }
                }
                override fun onFailure(call: Call<UID>, t: Throwable) {
                    Log.d("Post","fail $t")
                }
            })
            return successful
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
                        Log.e("login","success but something error")
                        Log.e("login error code",response.code().toString())
                        Log.e("login error message",response.message())
                        Log.e("login error errorbody",response.errorBody()!!.string())
                        Log.e("login error body",response.body().toString())
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
                        Log.d("setPlan","success")
                    }
                    else{
                        Log.d("setPlan","success but something error")
                        Log.e("setPlan error code",response.code().toString())
                        Log.e("setPlan error message", response.message())
                    }
                }

                override fun onFailure(call: Call<Plan>, t: Throwable) {
                    Log.e("setPlan","fail")
                    Log.e("setPlan error message",t.message.toString())
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
        fun _getDownloadPlanUsingExecute(uid:String) : ArrayList<Plan>{
            val body = retrofitService.getDownloadPlan(uid).execute().body()
            if(body == null){
                return ArrayList<Plan>()
            }
            else return body
        }
        fun _getDownloadPlan(uid : String):ArrayList<Plan>{
            var myPlan = ArrayList<Plan>()
            retrofitService.getDownloadPlan(uid).enqueue(object : Callback<ArrayList<Plan>> {
                override fun onResponse(call: Call<ArrayList<Plan>>, response: Response<ArrayList<Plan>>) {
                    if (response.isSuccessful) {
                        Log.d("getDownLoadPlan test success", response.body().toString())
                        Log.d("getDownLoadPlan test success", response.body()!!.size.toString())
                        myPlan = response.body()!!
                    } else {
                        Log.d("getDownLoadPlan test", "success but something error")
                    }
                    Log.d("getDownloadPlan code",response.code().toString())
                }

                override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                    Log.d("getDownLoadPlan test", "fail")
                    Log.d("getDownloadPlan error code",t.message.toString())
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
                        Log.d("getSubscribingPortfolio test success", response.body().toString())
                        myPortfolio = response.body()!!
                    } else {
                        Log.e("getSubscribingPortfolio test", "success but something error")
                        Log.e("getSubscribingPortfolio error code", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Portfolio>>, t: Throwable) {
                    Log.d("getPortfolio test", "fail")
                }

            })
            return myPortfolio
        }

        fun _getPortfolioComment(porfolioID: String) : ArrayList<Comment>{
            var commentList = ArrayList<Comment>()
            retrofitService.getPortfolioComment(porfolioID).enqueue(object :Callback<ArrayList<Comment>>{
                override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {
                    if(response.isSuccessful){
                        Log.d("getPortfolioComment test success", response.body().toString())
                        commentList = response.body()!!
                    }else{
                        Log.d("getPortfolioComment test", "success but something error")
                    }
                }

                override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                    Log.d("getPortfolioComment test", "fail")
                    Log.d("왜 오류남", t.message.toString())
                }
            })
            return commentList
        }
//        fun _postPortfolioComment(portfolioID: String,comment: Comment) : Boolean{
//            var bool  = true
//            retrofitService.postPortfolioComment(portfolioID,comment).enqueue(object : Callback<Comment>{
//                override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
//                    if(response.isSuccessful){
//                        Log.d("getPortfolioComment test success", response.body().toString())
//                        bool = true
//                    }else{
//                        bool= false
//                        Log.d("getPortfolioComment test", "success but something error")
//                    }
//                }
//
//                override fun onFailure(call: Call<Comment>, t: Throwable) {
//                    Log.e("getPortfolioComment Test","error")
//                    bool= false
//                }
//
//            })
//            return bool
//        }
    }
}