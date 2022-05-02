package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.PlanPagerBasicBinding

class PlansInnerVPAdapter(private var planinnerList: ArrayList<PlanWorkout>): RecyclerView.Adapter<PlansInnerVPAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val selectExerciseBtn : Button = itemView.findViewById(R.id.select_exercise_btn)
        fun bind(item: PlanWorkout) {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_pager_basic, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planinnerList[position])
        var exerciseArray: Array<String> = arrayOf("예시1", "예시2", "예시3", "예시4")
        var selectExercise:String
        holder.selectExerciseBtn.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("운동 선택")
                .setItems(exerciseArray,
                    DialogInterface.OnClickListener { dialog, which ->
                        // 여기서 인자 'which'는 배열의 position을 나타냅니다.
                        holder.selectExerciseBtn.text = exerciseArray[which]
                        selectExercise = exerciseArray[which]
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return planinnerList.size
    }
}