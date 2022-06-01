package com.example.sofront

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sofront.databinding.ActivityPlanDetailViewBinding
import com.example.sofront.databinding.ActivityPortfolioBinding
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class PortfolioActivity : AppCompatActivity() {
    val commentList  = ArrayList<Comment>()
    val adapter = CommentAdapter(commentList, false)
    val defaultImg = R.drawable.gymdori
    private var mBinding: ActivityPortfolioBinding? = null
    private val binding get() = mBinding!!
    var state = false

    private var time = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val portfolio  = intent.getSerializableExtra("portfolio") as Portfolio
        initView(portfolio, binding)
        getPortfolioComment(portfolio.id.toString())

        val user = adapter.user
        val uid : String
        val userName : String
        if(user==null){
            uid = "tmpUid"
            userName = "tmpName"
        }
        else{
            uid = user.uid
//            userName = user.displayName!!
            userName = "나!"
        }

        binding.commentSaveBtn.setOnClickListener {
            val text = binding.commentInput.text.toString()
            val comment = Comment(portfolio.id.toString(),uid,userName,"방금 전",FirebaseAuth.getInstance().currentUser?.photoUrl.toString(),text,"")
            postPortfolioComment(comment,binding)
            //TODO("디비에 저장하고 불러오기")

            closeKeyboard()
            binding.commentInput.setText("")
        }

        binding.profileImgInPortfolio.setOnClickListener{
            if(state){
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("UID", portfolio.portfolioWriter)
                startActivity(intent)
            }else{
                Toast.makeText(this, "잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
            }
        }
        val recyclerView = binding.commentRc

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(VerticalItemDecorator(30))
    }
    private fun initView(portfolio: Portfolio, binding : ActivityPortfolioBinding){
        binding.portfolioTitle.text  = portfolio.title
        binding.portfolioContent.text = portfolio.content
        binding.portfolioCommentNum.text = portfolio.commentNum.toString()
        binding.portfolioLikeNum.text = portfolio.likeNum.toString()
        binding.portfolioDate.text = portfolio.date

        _getCertainProfile(portfolio.portfolioWriter)

        Glide.with(this)
            .load(portfolio.contentImage)
            .into(binding.portfolioImg)

        if(portfolio.liked){
            binding.like.setBackgroundResource(R.drawable.ic_heart_fill)
        }

        binding.portfolioImg.setOnClickListener{
            if(System.currentTimeMillis() - time < 900){
                val like = PortfolioLike(
                            portfolio.id.toString(),
                            FirebaseAuth.getInstance().uid.toString(),
                        )

                RetrofitService.retrofitService.postPortfolioLike(like).enqueue(object : Callback<PortfolioLike>{
                    override fun onResponse(call: Call<PortfolioLike>, response: Response<PortfolioLike>) {
                        if(response.isSuccessful){
                            if(portfolio.liked) {
                                binding.like.setBackgroundResource(R.drawable.ic_heart)
                                val likeNum = binding.portfolioLikeNum.text.toString()
                                binding.portfolioLikeNum.text = (likeNum.toInt() -1).toString()
                                portfolio.liked = false
                            }
                            else {
                                binding.like.setBackgroundResource(R.drawable.ic_heart_fill)
                                val likeNum = binding.portfolioLikeNum.text.toString().toInt() +1
                                binding.portfolioLikeNum.text = likeNum.toString()
                                portfolio.liked = true
                            }
                            Log.d("postPortfolioLike","success")
                        }
                        else{
                            Log.e("postPortfolioLike","success but error")
                            Log.e("postPortfolioLike error code", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<PortfolioLike>, t: Throwable) {
                        Log.e("postPortfolioLike","fail")
                        Log.e("postPortfolioLike error msg",t.message!!)
                    }

                })
            }
            else
            time = System.currentTimeMillis()
        }
    }
    private fun closeKeyboard()
    {
        val view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private fun postPortfolioComment(comment: Comment, binding: ActivityPortfolioBinding){
        RetrofitService.retrofitService.postPortfolioComment(comment).enqueue(object :
            Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if(response.isSuccessful){
                    Log.d("postPortfolioComment test success", response.body().toString())

                    val commentNum = binding.portfolioCommentNum.text.toString().toInt()+1
                    binding.portfolioCommentNum.text = commentNum.toString()
                    comment.commentN = response.body()?.commentN.toString()
                    commentList.add(comment)
                    adapter.notifyItemInserted(adapter.itemCount-1)

                }else{
                    Log.e("postPortfolioComment test", "success but something error")
                    Log.e("postPortfolioComment error code",response.code().toString())
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e("postPortfolioComment Test","error")
                Log.e("postPortfolioComment error message",t.message.toString())
            }

        })
    }
    private fun getPortfolioComment(portfolioID: String) {
        RetrofitService.retrofitService.getPortfolioComment(portfolioID).enqueue(object :Callback<ArrayList<Comment>>{
            override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {
                if(response.isSuccessful){
                    Log.d("getPortfolioComment test success", response.body().toString())
//                    commentList = response.body()!!
                    for(comment in response.body()!!){
                        commentList.add(comment)
                        adapter.notifyItemInserted(adapter.itemCount-1)
                    }
                }else{
                    Log.e("getPortfolioComment test", "success but something error")
                    Log.e("getPortfolioComment error code",response.code().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                Log.d("getPortfolioComment test", "fail")
                Log.d("왜 오류남", t.message.toString())
            }
        })
    }

    fun _getCertainProfile(UID:String){
        var certainProfile:certainProfile
        RetrofitService.retrofitService.getCertainProfile(UID).enqueue(object : Callback<certainProfile> {
            override fun onResponse(call: Call<certainProfile>, response: Response<certainProfile>) {
                if (response.isSuccessful) {
                    Log.d("getProfile in Plan test", "success")
                    Log.d("getProfile in Plan success", response.body().toString())
                    certainProfile = response.body()!!
                    Log.d("프로필", certainProfile.toString())
                    binding.profileNameInPortfolio.text = certainProfile.name
                    openImg(certainProfile.profileImg)
                } else {
                    Log.d("getProfile in Plan test", "success but something error")
                }
            }

            override fun onFailure(call: Call<certainProfile>, t: Throwable) {
                Log.d("getProfile in Plan test", "fail")
            }
        })
    }

    fun openImg(profileImg:String){
        Glide.with(this)
            .load(profileImg) // 불러올 이미지 url
            .placeholder(defaultImg) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImg) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImg) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(binding.profileImgInPortfolio) // 이미지를 넣을 뷰
        state = true
    }
}