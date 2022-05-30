package com.example.sofront

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sofront.databinding.ActivityPortfolioBinding
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class PortfolioActivity : AppCompatActivity() {
    val commentList  = ArrayList<Comment>()
    val adapter = CommentAdapter(commentList)

    private var time = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPortfolioBinding.inflate(layoutInflater)
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
        Glide.with(this)
            .load(portfolio.contentImage)
            .into(binding.portfolioImg)
        if(portfolio.liked){
            binding.like.setBackgroundResource(R.drawable.ic_heart_fill)
        }

        binding.portfolioImg.setOnClickListener{
            if(System.currentTimeMillis() - time < 900){
                    postPortfolioLike(
                        PortfolioLike(
                            portfolio.id.toString(),
                            FirebaseAuth.getInstance().uid.toString(),
                        ), binding, portfolio.liked
                    )
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
                    commentList.add(comment)
                    val commentNum = binding.portfolioCommentNum.text.toString().toInt()+1
                    binding.portfolioCommentNum.text = commentNum.toString()
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
    private fun postPortfolioLike(like : PortfolioLike, binding: ActivityPortfolioBinding, liked : Boolean){
        RetrofitService.retrofitService.postPortfolioLike(like).enqueue(object : Callback<PortfolioLike>{
            override fun onResponse(call: Call<PortfolioLike>, response: Response<PortfolioLike>) {
                if(response.isSuccessful){
                    if(liked) {
                        binding.like.setBackgroundResource(R.drawable.ic_heart)
                        val likeNum = binding.portfolioLikeNum.text.toString()
                        binding.portfolioLikeNum.text = (likeNum.toInt() -1).toString()
                    }
                    else {
                        binding.like.setBackgroundResource(R.drawable.ic_heart_fill)
                        val likeNum = binding.portfolioLikeNum.text.toString().toInt() +1
                        binding.portfolioLikeNum.text = likeNum.toString()
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
}