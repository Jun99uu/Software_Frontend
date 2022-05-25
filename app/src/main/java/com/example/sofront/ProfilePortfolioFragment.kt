package com.example.sofront

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentProfilePortfolioBinding
import com.example.sofront.databinding.ProfilePortfolioItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Blob

class ProfilePortfolioFragment : Fragment() {
    lateinit var binding: FragmentProfilePortfolioBinding
    lateinit var recyclerView : RecyclerView
    lateinit var portfolioList : ArrayList<Portfolio>
    lateinit var adapter : ProfilePortfolioRecyclerViewAdapter
    val user = Firebase.auth.currentUser
    val myUid = user?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfilePortfolioBinding.inflate(inflater,container,false)
        recyclerView = binding.profilePortfolioRv

        tmpCallback(callback = {
            adapter.deleteFirstItem()
            if(portfolioList.size > 0){
                for(portfolio in portfolioList){
                    adapter.addItem(portfolio)
                }
            }
            adapter.notifyDataSetChanged()
            binding.noViewLayout.visibility = View.VISIBLE
        })

        setRecyclerView()
        return binding.root
    }

    fun setRecyclerView(){
        adapter = ProfilePortfolioRecyclerViewAdapter()
        setRecyclerView(adapter)
        setRecyclerViewAdapter(adapter)
    }

    fun setRecyclerView(adapter:ProfilePortfolioRecyclerViewAdapter){
        val auth = FirebaseAuth.getInstance()
        val hashTagList = ArrayList<String>()
        hashTagList.add("hashTag1")
        val commentList = ArrayList<Comment>()
        commentList.add(Comment("index","uid","프사","2022-08-23","프사", "댓글입니다"))
        commentList.add(Comment("index","uid","프사","2022-08-31","프사", "댓글입니다"))
        adapter.addItem(Portfolio(12,"제목1","이준규","이준규", false,"날이 좋아","2022-08-22", 5, 5,"https://gymggun.s3.ap-northeast-2.amazonaws.com/None/default.png", "https://gymggun.s3.ap-northeast-2.amazonaws.com/None/background_default.jpg"))
    }

    fun setRecyclerViewAdapter(adapter: ProfilePortfolioRecyclerViewAdapter){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    fun _getPortfolio(uid:String){
        var myPortfolio = ArrayList<Portfolio>()
        RetrofitService.retrofitService.getPortfolio(uid).enqueue(object  :
            Callback<ArrayList<Portfolio>> {
            override fun onResponse(
                call: Call<ArrayList<Portfolio>>,
                response: Response<ArrayList<Portfolio>>
            ) {
                if (response.isSuccessful) {
                    Log.d("getPortfolio test success", response.body().toString())
                    if(response.body() == null){
                        myPortfolio = response.body()!!
                    }
                    portfolioList = myPortfolio
                } else {
                    Log.d("getPortfolio test", "success but something error")
                }
            }

            override fun onFailure(call: Call<ArrayList<Portfolio>>, t: Throwable) {
                Log.d("getPortfolio test", "fail")
            }

        })
    }

    fun tmpCallback(callback: ()->Unit){
        _getPortfolio(myUid)
        Handler().postDelayed({
            callback()
        }, 810L)
    }
}