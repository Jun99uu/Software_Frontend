package com.example.sofront

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "5631e59a561b6e76e9d90caceb74d27e")
    }
}