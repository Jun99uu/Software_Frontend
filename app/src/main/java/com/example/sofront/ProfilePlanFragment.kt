package com.example.sofront

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.FragmentProfilePlanBinding


class ProfilePlanFragment : Fragment() {
    private var planList = ArrayList<Plan>()
    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }
    private lateinit var adapter: ProfilePlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfilePlanBinding.inflate(layoutInflater)


        //임시데이터
        //통신해서 planlist 채워야함
        val tmpHash = ArrayList<String>()
        tmpHash.add("안녕")
        tmpHash.add("그래")
        tmpHash.add("잘가")
        tmpHash.add("하하")
        tmpHash.add("깃허브")
        val routineList = ArrayList<Routine>()
        val initSet = ArrayList<Set>()
        initSet.add(Set(1,10))
        initSet.add(Set(1,10))
        initSet.add(Set(1,10))
        initSet.add(Set(1,10))
        initSet.add(Set(1,10))
        val initWorkout = ArrayList<Workout>()
        initWorkout.add(Workout("바보운동", 5, initSet))
        initWorkout.add(Workout("바보운동", 5, initSet))
        initWorkout.add(Workout("바보운동", 5, initSet))
        routineList.add(Routine(false, initWorkout))
        routineList.add(Routine(false, initWorkout))
        routineList.add(Routine(false, initWorkout))
        routineList.add(Routine(false, initWorkout))
        val tmpPlan = Plan("하이", tmpHash, routineList, "사람uid", false, 0,0 ,0)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        //임시데이터

        binding.profilePlanRecycler.layoutManager = linearLayoutManager
        adapter = ProfilePlanAdapter(planList)
        binding.profilePlanRecycler.adapter = adapter
        adapter.notifyDataSetChanged()
        binding.profilePlanRecycler.addItemDecoration(VerticalItemDecorator(30))

        return binding.root
    }

    fun listenPlan(listendPlans:ArrayList<Plan>){
        //서버에서 받아온 플랜리스트 여기에 파라미터로 넣어주기
        for(plan in listendPlans){
//            planList.add(plan)
        }
    }

}