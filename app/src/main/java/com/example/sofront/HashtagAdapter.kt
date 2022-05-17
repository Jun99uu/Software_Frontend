package com.example.sofront

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class HashtagAdapter(val itemList: ArrayList<String>): RecyclerView.Adapter<HashtagAdapter.ViewHolder>() {
    lateinit var context: Context

    // (1) 아이템 레이아웃과 결합
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashtagAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hashtag_item, parent, false)
        context = parent.context
        return ViewHolder(view)
    }
    // (2) 리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return itemList.size
    }
    // (3) View에 내용 입력
    override fun onBindViewHolder(holder: HashtagAdapter.ViewHolder, position: Int) {
        holder.tagBtn.text = itemList[position]
        holder.tagBtn.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }
    // (4) 레이아웃 내 View 연결
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tagBtn: Button = itemView.findViewById(R.id.btn_tag)
    }

    // (5) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (6) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
    // (7) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}