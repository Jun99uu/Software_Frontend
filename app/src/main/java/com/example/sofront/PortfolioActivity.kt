package com.example.sofront

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityPortfolioBinding
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class PortfolioActivity : AppCompatActivity() {
    val commentList  = ArrayList<Comment>()
    val adapter = CommentAdapter(commentList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val portfolio  = intent.getSerializableExtra("portfolio") as Portfolio
        initView(portfolio,binding)
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
            userName = "tmp"
        }

        binding.commentSaveBtn.setOnClickListener {
            val text = binding.commentInput.text.toString()
            val comment = Comment(portfolio.id.toString(),uid,userName, Date().toString(),"",text,"")
            postPortfolioComment(comment)
            //TODO("디비에 저장하고 불러오기")

            CloseKeyboard()
            binding.commentInput.setText("")
        }

        val recyclerView = binding.commentRc

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(VerticalItemDecorator(30))
    }
    fun initView(portfolio: Portfolio, binding: ActivityPortfolioBinding){
        binding.portfolioTitle.text  = portfolio.title
        binding.portfolioContent.text = portfolio.content
        binding.portfolioCommentNum.text = portfolio.commentNum.toString()
        binding.portfolioLikeNum.text = portfolio.likeNum.toString()
        if(portfolio.liked){
            binding.like.setBackgroundColor(Color.RED)
        }
    }
    fun CloseKeyboard()
    {
        val view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    fun postPortfolioComment(comment: Comment){
        RetrofitService.retrofitService.postPortfolioComment(comment).enqueue(object :
            Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if(response.isSuccessful){
                    Log.d("postPortfolioComment test success", response.body().toString())
                    commentList.add(comment)
                    adapter.notifyDataSetChanged()
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
    fun getPortfolioComment(porfolioID: String) {
        RetrofitService.retrofitService.getPortfolioComment(porfolioID).enqueue(object :Callback<ArrayList<Comment>>{
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
}