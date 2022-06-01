package com.example.sofront

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentHeartBinding
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeartFragment : Fragment() {
    lateinit var binding : FragmentHeartBinding
    lateinit var recyclerView: RecyclerView
    val portfolioList = ArrayList<Portfolio>()
    val adapter = ProfilePortfolioRecyclerViewAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeartBinding.inflate(inflater,container,false)
        setRecyclerView()

        return binding.root
    }
    fun setRecyclerView(){
        recyclerView = binding.heartPortfolioRv
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        getSubscribingPortfolio(FirebaseAuth.getInstance().uid!!)
    }

    fun getSubscribingPortfolio(uid:String) {
        RetrofitService.retrofitService.getSubscribingPortfolio(uid).enqueue(object  :
            Callback<ArrayList<Portfolio>> {
            override fun onResponse(
                call: Call<ArrayList<Portfolio>>,
                response: Response<ArrayList<Portfolio>>
            ) {
                if (response.isSuccessful) {
                    if(response.body()!!.size==0){
                        binding.heartPortfolioRv.visibility = View.GONE
                        binding.noSubscribe.visibility = View.VISIBLE

                    }
                    Log.d("getSubscribingPortfolio test success", response.body().toString())
                    for(item in response.body()!!) {
                        adapter.addItem(item)
                        adapter.notifyItemInserted(adapter.itemCount-1)
                    }
                } else {
                    Log.e("getSubscribingPortfolio test", "success but something error")
                    Log.e("getSubscribingPortfolio error code", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<Portfolio>>, t: Throwable) {
                Log.d("getPortfolio test", "fail")
            }

        })
    }
}