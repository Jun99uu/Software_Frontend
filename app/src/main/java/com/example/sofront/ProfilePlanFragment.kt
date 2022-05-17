package com.example.sofront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentProfilePlanBinding


class ProfilePlanFragment : Fragment() {
    val planList = ArrayList<Plan>()
    val tmpHash = ArrayList<String>()
    val routineList = ArrayList<Routine>()
    val profilePlanAdapter = ProfilePlanAdapter(planList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfilePlanBinding.inflate(layoutInflater)

        //리사이클러뷰 테스트를 위한 임시 데이터
        tmpHash.add("하이")
        tmpHash.add("바이")
        tmpHash.add("아오")
        val initSet = ArrayList<Set>()
        initSet.add(Set(0,0))
        val initWorkout = ArrayList<Workout>()
        initWorkout.add(Workout("", 0, initSet))
        routineList.add(Routine(false, initWorkout))
        val tmpPlan = Plan("하이", tmpHash, routineList, "사람uid", false, 0,0 ,0)
        //임시데이터 끝

        binding.profilePlanRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        profilePlanAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.profilePlanRecycler.adapter = profilePlanAdapter

        planList.add(tmpPlan)
        planList.add(tmpPlan)
        planList.add(tmpPlan)
        profilePlanAdapter.notifyDataSetChanged()

        return binding.root
    }
}