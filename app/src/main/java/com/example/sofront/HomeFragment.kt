package com.example.sofront

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.sofront.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val quotes = Quotes()
        val quoLen = quotes.title.size-1
        val range = (0..quoLen).random()
        binding.title.text = quotes.title[range]
        for(i in 0..2){
            when(i){
                0 -> binding.firstUpperBtn.text = quotes.hashtag[range][i]
                1 -> binding.secondUpperBtn.text = quotes.hashtag[range][i]
                2 -> binding.thirdUpperBtn.text = quotes.hashtag[range][i]
            }
        }

        var firstUpper = false
        var secondUpper = false
        var thirdUpper = false

        binding.firstUpperBtn.setOnClickListener{
            if(firstUpper){
                binding.firstUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_no_select_btn)
                firstUpper = false
            }else{
                binding.firstUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_yes_select_btn)
                firstUpper = true
            }
        }
        binding.secondUpperBtn.setOnClickListener{
            if(secondUpper){
                binding.secondUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_yes_select_btn)
                secondUpper = false
            }else{
                binding.secondUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_no_select_btn)
                secondUpper = true
            }
        }
        binding.thirdUpperBtn.setOnClickListener{
            if(thirdUpper){
                binding.thirdUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_yes_select_btn)
                thirdUpper = false
            }else{
                binding.thirdUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_no_select_btn)
                thirdUpper = true
            }
        }

        binding.hamburger.setOnClickListener{
            val intent = Intent(context, SettingActivity::class.java)
            this.startActivity(intent)
        }

        binding.makePlanBtn.setOnClickListener{
            val makeIntent = Intent(context, MakePlanActivity::class.java)
            startActivity(makeIntent)
        }

        val activity = requireActivity() as HomeActivity
        binding.calendarBtn.setOnClickListener{
            val planFragment = ListFragment()
            activity.replaceFragment(planFragment, 0)
        }

        binding.viewPlanBtn.setOnClickListener{
            val planFragment = PlanCollectionFragment()
            activity.replaceFragment(planFragment, 1)
        }

        val clipboard: ClipboardManager = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        binding.metaBtn.setOnClickListener{
            val metaPwd = "9986"
            val clip = ClipData.newPlainText("label", metaPwd)
            clipboard.setPrimaryClip(clip)
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://link.ifland.ai/tNCX"))
            Toast.makeText(requireContext(), "메타버스 입장 비밀번호가 클립보드에 복사되었습니다!", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }

        return binding.root
    }

    private class Quotes() {
        var title = arrayOf("헬스장에 가기만 하면\n반은 성공이다💪", "끈기있는 자만이\n득근할 수 있다🏋️‍♀️", "운동 할 생각에\n가슴이 득근두근🤩", "지금의 1RM이\n워밍업이 되도록🔥")
        var hashtag = arrayOf(arrayOf("#오운완", "#할수있다", "#가보자고"), arrayOf("#끈기", "#열정", "#득근"), arrayOf("#기분이", "#설렘", "#득근두근"), arrayOf("#화이팅", "#1RM", "#워밍업"))
    }

    fun checkPackageExisting(packageName: String?): Boolean {
        if (packageName == null || packageName.length == 0) {
            return false
        }
        var bExist = false
        val pkgMgr: PackageManager = requireActivity().packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val mApps = pkgMgr.queryIntentActivities(mainIntent, 0)
        if (mApps.size > 0) {
            for (i in mApps.indices) {
                try {
                    if (mApps[i].activityInfo.packageName.startsWith(packageName)) {
                        bExist = true
                        break
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    bExist = false
                }
            }
        }
        return bExist
    }

}