package com.lessgenius.maengland

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import com.lessgenius.maengland.util.SoundUtil
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 앱이 사용 중일 때 화면 유지
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 사운드 초기화
        SoundUtil.init(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundUtil.release()
    }

}