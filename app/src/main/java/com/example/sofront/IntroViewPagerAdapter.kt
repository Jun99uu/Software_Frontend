package com.example.sofront

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroViewPagerAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa){
    var fragments = listOf<Fragment>() //프래그먼트가 저장될 배열

    override fun getItemCount(): Int = fragments.size //프래그먼트 배열의 크기

    override fun createFragment(position: Int): Fragment {
        return fragments.get(position); //반환될 프래그먼트
    }
}