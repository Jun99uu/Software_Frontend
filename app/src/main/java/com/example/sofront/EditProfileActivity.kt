package com.example.sofront

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sofront.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditProfileActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    var UID:String = "" //이전 액티비티에서 전달받은 uid
    var currentUID:String = "" //현재로그인된 유저의 uid
    var state:Boolean = false //편집 상황 _ false->편집버튼 클릭 전, true->편집버튼 클릭 후
    var afterNickname = ""
    var afterSubtitle = ""
    val converter = BitmapConverter()
    var afterProfileImg = "" //String
    var afterBackground = "" //String
    lateinit var profileImg:ImageView
    lateinit var backgroundImg:ImageView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = com.example.sofront.databinding.ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileImg = binding.profileImagePreview
        backgroundImg = binding.backgroundPreview

        UID = intent.getStringExtra("UID").toString()

        user?.let {
            currentUID = user.uid
            if(!currentUID.equals(UID)){
                //이전에 전달받은 uid와 현재 사용자의 uid가 일치하지 않을때 -> 예외처리일뿐
                Toast.makeText(this,"잘못된 접근입니다", Toast.LENGTH_LONG).show()
                ActivityCompat.finishAffinity(this) //해당 액티비티 종료
            }
        }

        val prevNickname = intent.getStringExtra("nickname").toString()
        val prevSubtitle = intent.getStringExtra("subtitle").toString()
//        val prevProfileImg = intent.getStringExtra("profile")!! //스트링으로 받음
//        val prevBackground = intent.getStringExtra("background")!! //스트링으로 받음
        binding.profileEditTitle.text = "${prevNickname}님의\n프로필입니다🔥"
        binding.profileEditNickname.hint = "${prevNickname}"
        binding.profileEditSubtitle.hint = "${prevSubtitle}"
//        binding.profileImagePreview?.setImageBitmap(converter.stringToBitmap(prevProfileImg))
//        binding.backgroundPreview?.setImageBitmap(converter.stringToBitmap(prevBackground))
        afterSubtitle = prevSubtitle
        afterNickname = prevNickname

        binding.editCancleBtn.setOnClickListener{
            if(!state){
                //아직 편집버튼 안누른 상태
                onBackPressed()
            }else{
                canclePressed()
            }
        }

        binding.profileImagePreview.setOnClickListener{
            if(state){
                val PIintent = Intent(Intent.ACTION_GET_CONTENT)
                PIintent.setType("image/*")
                profileActivityLauncher.launch(PIintent)
            }
        }

        binding.backgroundPreview.setOnClickListener{
            if(state){
                val BIintent = Intent(Intent.ACTION_GET_CONTENT)
                BIintent.setType("image/*")
                backgroundActivityLauncher.launch(BIintent)
            }
        }

        binding.editSaveBtn.setOnClickListener{
            if(!state){
                state = true
                binding.editSaveBtn.text = "저장"
                binding.editSaveBtn.background = ContextCompat.getDrawable(this, R.drawable.red_radius)
                binding.profileEditNickname.isEnabled = true
                binding.profileEditSubtitle.isEnabled = true
                binding.profileEditNickname.setText(prevNickname)
                binding.profileEditSubtitle.setText(prevSubtitle)
            }else{
                state = false
                afterNickname = binding.profileEditNickname.text.toString()
                afterSubtitle = binding.profileEditSubtitle.text.toString()
//                if(afterProfileImg.equals("")) afterProfileImg = prevProfileImg
//                if(afterBackground.equals("")) afterBackground = prevBackground
                binding.editSaveBtn.text = "편집"
                binding.editSaveBtn.background = ContextCompat.getDrawable(this, R.drawable.blue_radius)
                binding.imgEditBtn.visibility = View.INVISIBLE
                binding.profileEditNickname.isEnabled = false
                binding.profileEditSubtitle.isEnabled = false
                savePressed()
            }
        }
    }

    fun canclePressed(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("정말로 취소하시겠습니까?")
            .setMessage("수정하던 내용은 복구하실 수 없습니다.\n정말로 수정을 취소하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //확인클릭
                    Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                    ActivityCompat.finishAffinity(this)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //취소클릭
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    fun savePressed(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("수정사항을 저장하시겠습니까?")
            .setMessage("이전의 내용은 복구하실 수 없습니다.\n정말로 저장하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //확인클릭
                    //afterProfileImg, afterBackground, afterNickname, afterSubtitle로 서버에 저장
                    //서버 저장 성공시 액티비티 종료
                    val sendData = editProfile(UID, afterProfileImg, afterBackground, afterNickname, afterSubtitle)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //취소클릭
                    Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    private val profileActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK && it.data !=null) {
                var currentImageUri = it.data?.data
                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            profileImg?.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            profileImg?.setImageBitmap(bitmap)
                            afterProfileImg = converter.bitmapToString(bitmap)
                        }
                    }

                }catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == RESULT_CANCELED){
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }else{
                Log.d("ActivityResult","something wrong")
            }
        }

    private val backgroundActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK && it.data !=null) {
                var currentImageUri = it.data?.data
                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            backgroundImg?.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            backgroundImg?.setImageBitmap(bitmap)
                            afterBackground = converter.bitmapToString(bitmap)
                        }
                    }

                }catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == RESULT_CANCELED){
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }else{
                Log.d("ActivityResult","something wrong")
            }
        }

    private fun requestPermissions() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Log.d("권한요청", "$it")
        }.launch(PERMISSIONS_REQUESTED)
    }

    companion object {
        private const val PERMISSION_READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val PERMISSION_WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        private val PERMISSIONS_REQUESTED: Array<String> = arrayOf(
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE,
        )
    }
}