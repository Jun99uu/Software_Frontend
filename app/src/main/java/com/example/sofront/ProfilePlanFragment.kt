package com.example.sofront

import android.R
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityProfileBinding
import com.example.sofront.databinding.FragmentProfilePlanBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfilePlanFragment : Fragment() {
    private var mBinding: FragmentProfilePlanBinding? = null
    private val binding get() = mBinding!!
    private var planList = ArrayList<Plan>()
    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }
    private lateinit var adapter: ProfilePlanAdapter
    val myUid = Firebase.auth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProfilePlanBinding.inflate(layoutInflater)

        if(myUid != null){
            _getPlanByUid()
        }else{
            Log.d("uid", "null value")
        }

        return binding.root
    }

    fun listenPlan(listendPlans:ArrayList<Plan>){
        //서버에서 받아온 플랜리스트 여기에 파라미터로 넣어주기
        binding.profilePlanRecycler.layoutManager = linearLayoutManager
        adapter = ProfilePlanAdapter(listendPlans)
        binding.profilePlanRecycler.adapter = adapter
        adapter.notifyDataSetChanged()
        binding.profilePlanRecycler.addItemDecoration(VerticalItemDecorator(30))
        binding.noViewLayout.visibility = View.GONE
        binding.profilePlanRecycler.visibility = View.VISIBLE
    }

    fun _getPlanByUid(){
        var myPlan = ArrayList<Plan>()
        RetrofitService.retrofitService.getMyPlanInProfile(myUid!!, myUid).enqueue(object : Callback<ArrayList<Plan>> {
            override fun onResponse(call: Call<ArrayList<Plan>>, response: Response<ArrayList<Plan>>) {
                if (response.isSuccessful) {
                    Log.d("getPlan test", "success")
                    myPlan = response.body()!!
                    Log.d("씨이발", response.body().toString())
                    if(myPlan.size > 0){
                        listenPlan(myPlan)
                    }
                } else {
                    Log.d("getPlan test", "success but something error")
                }
                planList = myPlan
            }

            override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                Log.d("getPlan test", "fail")
            }
        })
    }

}