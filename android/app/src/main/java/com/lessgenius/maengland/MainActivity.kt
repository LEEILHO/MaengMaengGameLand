package com.lessgenius.maengland

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: ")
        initSensorManager()
    }

    override fun onResume() {
        super.onResume()
        //
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(listener)
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    fun initSensorManager() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }

    // 메인 쓰레드에서 돌아감
    val listener = object : SensorEventListener {
        // 센서 바뀔 때마다 불림
        override fun onSensorChanged(event: SensorEvent) {
            Log.d(TAG, "onSensorChanged: x: ${event.values[0]}, y: ${event.values[1]}")

//            list.map {
//                it.imageView.x -= it.speed * event.values[0]
//                it.imageView.y += it.speed * event.values[1]
//
//                if (it.imageView.x < 0) {
//                    it.imageView.x = 0F
//                }
//                if (it.imageView.y < 0) {
//                    it.imageView.y = 0F
//                }
//
//                // x축 범위 벗어나지 않게 맞춰줌
//                if (it.imageView.x > realX - SIZE) {
//                    it.imageView.x = (realX - SIZE).toFloat()
//                }
//                // y 축은 타이틀 사이즈 이런 거 계산해서 빼줘야 함
//                if (it.imageView.y > realY - 400 - SIZE) {
//                    it.imageView.y = (realY - 400 - SIZE).toFloat()
//                }
//            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

    }

}