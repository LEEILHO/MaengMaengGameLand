package com.lessgenius.maengland.util

import android.content.Context
import android.media.SoundPool
import com.lessgenius.maengland.R

object SoundUtil {

    private var soundPool: SoundPool? = null
    private var jumpSound: Int? = null
    private var clickSound: Int? = null

    fun init(context: Context) {
        if (soundPool == null) {
            soundPool = SoundPool.Builder().build()
        }

        jumpSound = soundPool?.load(context, R.raw.sound_jump, 1)
        clickSound = soundPool?.load(context, R.raw.sound_push_button, 1)

    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }

    fun playJumpSound() {
        soundPool?.play(jumpSound!!, 1F, 1F, 0, 0, 1F)
    }

    fun playClickSound() {
        soundPool?.play(clickSound!!, 1F, 1F, 0, 0, 1F)
    }
}