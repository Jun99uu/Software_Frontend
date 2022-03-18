package com.example.sofront

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sofront.databinding.FragmentSignInBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SignInBottomSheet : BottomSheetDialogFragment() {
    //프래그먼트에서는 context를 상속받지 않기 때문에 임의로 만들어줌
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSignInBottomSheetBinding.inflate(inflater, container, false)
        binding.signUp.setOnClickListener{
            val intent = Intent(mainActivity, SignUpAuth::class.java)
            startActivity(intent)
//            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//            mainActivity.finish()
        }
        binding.signIn.setOnClickListener{
            var intent = Intent(mainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    //여기 아래는 안봐도됨 _ sheet의 크기 설정하기 위한 부분

    companion object {
        const val TAG = "SignInBottomSheet"
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
        return getWindowHeight() * 70 / 100
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}