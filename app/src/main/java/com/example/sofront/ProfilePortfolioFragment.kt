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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Blob

class ProfilePortfolioFragment : Fragment() {
    lateinit var binding: FragmentProfilePortfolioBinding
    lateinit var recyclerView : RecyclerView
    lateinit var portfolioList : ArrayList<Portfolio>
    lateinit var adapter : ProfilePortfolioRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfilePortfolioBinding.inflate(inflater,container,false)
        recyclerView = binding.profilePortfolioRv

        tmpCallback(callback = {
            for(portfolio in portfolioList){
                adapter.addItem(portfolio)
            }
            Log.d("제발제발", portfolioList.toString())
            adapter.notifyDataSetChanged()
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
        if(auth.uid==null){
//            val hashTagList = ArrayList<String>()
//            hashTagList.add("hashTag1")
//            val commentList = ArrayList<portfolioComment>()
//            commentList.add(portfolioComment("코멘트 작성자","uid","프사","2022-08-23","commentContent"))
//            commentList.add(portfolioComment("댓글작성자 박재현","uid","프사","2022-08-31","댓글내용 : 날이 꾸리꾸리"))
//            adapter.addItem(Portfolio("제목1","이준규","날이 좋아","2022-08-22", hashTagList, 10))
//            adapter.addItem(Portfolio("제목2","박성규","날이 안좋아","2022-08-17",hashTagList,10))
        }
        else{
            _getPortfolio(auth.uid!!)
            for(portfolio in portfolioList){
                adapter.addItem(portfolio)
            }
        }
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
                    myPortfolio = response.body()!!
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
        _getPortfolio("류승민")
        Handler().postDelayed({
            callback()
        }, 3000L)
    }

}