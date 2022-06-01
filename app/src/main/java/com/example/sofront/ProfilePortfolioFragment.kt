package com.example.sofront

import android.content.Context
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
import com.example.sofront.databinding.ActivityProfileBinding
import com.example.sofront.databinding.FragmentProfilePortfolioBinding
import com.example.sofront.databinding.ProfilePortfolioItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Blob

class ProfilePortfolioFragment(val myUid : String) : Fragment() {
    private var mBinding: FragmentProfilePortfolioBinding? = null
    private val binding get() = mBinding!!

    lateinit var recyclerView : RecyclerView
    lateinit var portfolioList : ArrayList<Portfolio>
    lateinit var adapter : ProfilePortfolioRecyclerViewAdapter
//    val user = Firebase.auth.currentUser
//    val myUid = user?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentProfilePortfolioBinding.inflate(layoutInflater)
        recyclerView = binding.profilePortfolioRv
        setRecyclerView()
        _getPortfolio(myUid)
        return binding.root
    }

    fun setRecyclerView(){
        adapter = ProfilePortfolioRecyclerViewAdapter()
        setRecyclerViewAdapter(adapter)
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
                    if(response.body() != null){
                        myPortfolio = response.body()!!
                        portfolioList = myPortfolio
                        updatePortfolio()
                    }
                } else {
                    Log.e("getPortfolio test", "success but something error")
                    Log.e("getPortfolio error code",response.code().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<Portfolio>>, t: Throwable) {
                Log.e("getPortfolio", "fail")
                Log.e("getPortfolio",t.message.toString())
            }

        })
    }

    fun updatePortfolio(){
        if(portfolioList.size > 0){
            for(portfolio in portfolioList){
                adapter.addItem(portfolio)
            }
            binding.noViewLayout.visibility = View.GONE
            binding.profilePortfolioRv.visibility = View.VISIBLE
        }else{
            binding.noViewLayout.visibility = View.VISIBLE
            binding.profilePortfolioRv.visibility = View.GONE
        }
        adapter.notifyDataSetChanged()
    }
}