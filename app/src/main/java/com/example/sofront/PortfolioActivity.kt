package com.example.sofront

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityPortfolioBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

class PortfolioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val portfolio  = intent.getSerializableExtra("portfolio") as Portfolio
        binding.portfolioTitle.text  = portfolio.title
        binding.portfolioContent.text = portfolio.content
        binding.portfolioCommentNum.text = portfolio.commentNum.toString()
        val commentArray = ArrayList<Comment>()
        val adapter = CommentAdapter(commentArray)
        binding.portfolioLikeNum.text = portfolio.likeNum.toString()
        binding.commentSaveBtn.setOnClickListener {
            val text = binding.commentInput.text.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val uid : String
            val userName : String
            if(user==null){
                uid = "tmpUid"
                userName = "tmpName"
            }
            else{
                uid = user.uid
                userName = user.displayName!!
            }
            val comment = Comment(portfolio.title,uid,userName, Date().toString(),"",text)
//            RetrofitService._postPortfolioComment(comment)
            //TODO("디비에 저장하고 불러오기")
            commentArray.add(comment)
            adapter.notifyDataSetChanged()
            CloseKeyboard()
            binding.commentInput.setText("")
        }
        val recyclerView = binding.commentRc

        commentArray.add(TestFactory.getSomeComment())
        commentArray.add(TestFactory.getSomeComment())
        commentArray.add(TestFactory.getSomeComment())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(VerticalItemDecorator(30))
    }
    fun CloseKeyboard()
    {
        var view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}