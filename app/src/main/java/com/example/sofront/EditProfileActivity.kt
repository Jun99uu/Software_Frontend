package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.example.sofront.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URI

class EditProfileActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    val currentUID = user!!.uid //현재로그인된 유저의 uid
    var state:Boolean = false //편집 상황 _ false->편집버튼 클릭 전, true->편집버튼 클릭 후
    var afterNickname = ""
    var afterSubtitle = ""
    val converter = BitmapConverter()
    var afterProfileImg = "" //String
    var afterBackground = "" //String
    lateinit var profileImg:ImageView
    lateinit var backgroundImg:ImageView
    val defaultImg = R.drawable.gymdori
    val defaultBack = R.drawable.womanrun

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileImg = binding.profileImagePreview
        backgroundImg = binding.backgroundPreview

        val prevNickname = intent.getStringExtra("nickname").toString()
        val prevSubtitle = intent.getStringExtra("subtitle").toString()
        val prevProfileImg = intent.getStringExtra("profileImg").toString()
        val prevBackground = intent.getStringExtra("background").toString()
        binding.profileEditTitle.text = "${prevNickname}님의\n프로필입니다🔥"
        binding.profileEditNickname.hint = "${prevNickname}"
        binding.profileEditSubtitle.hint = "${prevSubtitle}"
        Glide.with(this)
            .load(prevProfileImg) // 불러올 이미지 url
            .placeholder(defaultImg) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImg) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImg) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(binding.profileImagePreview) // 이미지를 넣을 뷰

        Glide.with(this)
            .load(prevBackground) // 불러올 이미지 url
            .placeholder(defaultBack) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultBack) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultBack) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(binding.backgroundPreview) // 이미지를 넣을 뷰


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

        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

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
                binding.editSaveBtn.text = "편집"
                binding.editSaveBtn.background = ContextCompat.getDrawable(this, R.drawable.blue_radius)
                binding.imgEditBtn.visibility = View.INVISIBLE
                binding.profileEditNickname.isEnabled = false
                binding.profileEditSubtitle.isEnabled = false
                savePressed(intent)
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

    fun savePressed(intent : Intent){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("수정사항을 저장하시겠습니까?")
            .setMessage("이전의 내용은 복구하실 수 없습니다.\n정말로 저장하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //확인클릭
                    //afterProfileImg, afterBackground, afterNickname, afterSubtitle로 서버에 저장
                    //서버 저장 성공시 액티비티 종료
                    val sendData = editProfile(currentUID, afterProfileImg, afterBackground, afterNickname, afterSubtitle)
                    _editProfile(sendData, intent)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //취소클릭
                    Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                            afterProfileImg = converter.bitmapToString(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
//                            val bitmap = ImageDecoder.decodeBitmap(source)
                            val bitmap = currentImageUri.uriToBitmap(this)
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

    @RequiresApi(Build.VERSION_CODES.O)
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
                            afterBackground = converter.bitmapToString(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
//                            val bitmap = ImageDecoder.decodeBitmap(source)
                            val bitmap = currentImageUri.uriToBitmap(this)
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

    fun _editProfile(editProfile:editProfile, intent:Intent){
        RetrofitService.retrofitService.editProfile(currentUID, editProfile).enqueue(object : Callback<editProfile> {
            override fun onResponse(call: Call<editProfile>, response: Response<editProfile>) {
                if(response.isSuccessful){
                    Log.d("editProfile test success", response.body().toString())
                    startActivity(intent)
                }else{
                    Log.d("editProfile test", "success but something error")
                }
            }
            override fun onFailure(call: Call<editProfile>, t: Throwable) {
                Log.d("editPortfolio test", "fail")
            }
        })
    }

    @Throws(IOException::class)
    fun Uri.uriToBitmap(context: Context): Bitmap =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(context.contentResolver, this)
            ) { decoder: ImageDecoder, _: ImageDecoder.ImageInfo?, _: ImageDecoder.Source? ->
                decoder.isMutableRequired = true
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            BitmapDrawable(
                context.resources,
                MediaStore.Images.Media.getBitmap(context.contentResolver, this)
            ).bitmap
        }
}