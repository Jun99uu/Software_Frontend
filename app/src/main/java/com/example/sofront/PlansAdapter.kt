package com.example.sofront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class PlansAdapter(
    private val planList: List<PlanData>
) : RecyclerView.Adapter<PlansAdapter.MyViewHolder>() {

    lateinit var context: Context

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(plan: PlanData) {
            //데이터저장
//            val txtName = itemView.findViewById<TextView>(R.id.txt_name)
//            val imgPhoto = itemView.findViewById<CircleImageView>(R.id.img_photo)
//            val imgMore = itemView.findViewById<ImageButton>(R.id.img_more)
//            val layoutExpand = itemView.findViewById<LinearLayout>(R.id.layout_expand)
//
//            txtName.text = person.name
//            imgPhoto.setImageResource(person.image)

//            bind().setOnClickListener {
//                // 1
//                val show = toggleLayout(!person.isExpanded, it, layoutExpand)
//                person.isExpanded = show
//            }

            val planSetting = itemView.findViewById<ConstraintLayout>(R.id.plan_setting)
            val toggleBtn: Button = itemView.findViewById(R.id.plan_toggle)
            toggleBtn.setOnClickListener{
                val show = toggleLayout(!plan.isExpanded, it, planSetting)
                plan.isExpanded = show
            }
        }

        private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: ConstraintLayout): Boolean {
            // 2
            ToggleAnimation.toggleArrow(view, isExpanded)
            if (isExpanded) {
                ToggleAnimation.expand(layoutExpand)
            } else {
                ToggleAnimation.collapse(layoutExpand)
            }
            return isExpanded
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planList[position])
    }

    override fun getItemCount(): Int {
        return planList.size
    }

}