package com.example.sofront

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.PlanRecyclerViewBinding

class PlanRecyclerViewAdapter : RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder>() {
    private val testList = mutableListOf<Plan>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanRecyclerViewAdapter.ViewHolder {
        val binding = PlanRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(testList[position])
    }

    override fun getItemCount(): Int {
        return testList.size
    }
    inner class ViewHolder(binding: PlanRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root){
        private val textView = binding.planListTv

        fun bind(item: Plan) {
            textView.text = item.planName
            textView.setOnLongClickListener { v ->
                val clipItem = ClipData.Item(item.routineList.size.toString() + "-" + item.planName)
                val dragData = ClipData(
//                    v.tag as? CharSequence,
                    item.routineList.size.toString() + "-" + item.planName,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem)
                val myShadow = MyDragShadowBuilder(v)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(dragData,myShadow,null,0)
                } else {
                    v.startDrag(dragData,myShadow,null,0)
                }
            }
        }
    }
    fun addItem(item:Plan){
        testList.add(item)
    }
}
private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

    private val shadow = ColorDrawable(Color.LTGRAY)

    // Defines a callback that sends the drag shadow dimensions and touch point
    // back to the system.
    override fun onProvideShadowMetrics(size: Point, touch: Point) {

        // Set the width of the shadow to half the width of the original View.
        val width: Int = view.width / 2

        // Set the height of the shadow to half the height of the original View.
        val height: Int = view.height / 2

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the
        // same as the Canvas that the system provides. As a result, the drag shadow
        // fills the Canvas.
        shadow.setBounds(0, 0, width, height)

        // Set the size parameter's width and height values. These get back to
        // the system through the size parameter.
        size.set(width, height)

        // Set the touch point's position to be in the middle of the drag shadow.
        touch.set(width / 2, height / 2)
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system
    // constructs from the dimensions passed to onProvideShadowMetrics().
    override fun onDrawShadow(canvas: Canvas) {

        // Draw the ColorDrawable on the Canvas passed in from the system.
        shadow.draw(canvas)
    }
}