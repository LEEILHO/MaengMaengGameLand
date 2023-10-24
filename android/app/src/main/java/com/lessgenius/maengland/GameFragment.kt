package com.lessgenius.maengland

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService


private const val TAG = "GameFragment_김진영"
class GameFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSensorManager()
    }


//    override fun onResume() {
//        super.onResume()
//        //
//        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        sensorManager.unregisterListener(listener)
//    }

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    private fun initSensorManager() {
        val sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
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