package com.example.sofront

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sofront.databinding.FragmentCalendarPlanBottomSheetListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    CalendarPlanBottomSheet.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class CalendarPlanBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentCalendarPlanBottomSheetListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarPlanBottomSheetListDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            // 다이얼로그 크기 설정 (인자값 : DialogInterface)
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 20 / 100
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = PlanRecyclerViewAdapter()
        val planList = ArrayList<Plan>()

        val tmpHash = ArrayList<String>()
        tmpHash.add("안녕")
        tmpHash.add("그래")
        tmpHash.add("잘가")
        tmpHash.add("하하")
        tmpHash.add("깃허브")
        val routineList = ArrayList<Routine>()
        val initSet = ArrayList<Set>()
        initSet.add(Set(0,0))
        val initWorkout = ArrayList<Workout>()
        initWorkout.add(Workout("", 0, initSet))
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
        for(plan in planList){
            adapter.addItem(plan)
        }
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
    }

    companion object {

        // TODO: Customize parameters
        fun newInstance(itemCount: Int): CalendarPlanBottomSheet =
            CalendarPlanBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}