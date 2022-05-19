package com.example.sofront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ProfilePlanHashtagAdapter(val itemList: ArrayList<String>): RecyclerView.Adapter<ProfilePlanHashtagAdapter.ViewHolder>() {
    lateinit var context: Context

    // (1) 아이템 레이아웃과 결합
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePlanHashtagAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_hashtag_item, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    // (2) 리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return itemList.size
    }

    // (3) View에 내용 입력
    override fun onBindViewHolder(holder: ProfilePlanHashtagAdapter.ViewHolder, position: Int) {
        holder.tagBtn.text = itemList[position]
    }

    // (4) 레이아웃 내 View 연결
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagBtn: Button = itemView.findViewById(R.id.btn_tag)
    }
}