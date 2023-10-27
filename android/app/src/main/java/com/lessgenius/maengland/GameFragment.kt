package com.lessgenius.maengland

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.wear.widget.SwipeDismissFrameLayout
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.databinding.FragmentGameBinding


private const val TAG = "GameFragment_김진영"

class GameFragment :
    BaseFragment<FragmentGameBinding>(FragmentGameBinding::bind, R.layout.fragment_game) {

    private lateinit var swipeCallback: SwipeDismissFrameLayout.Callback
    private lateinit var valueAnimator: ValueAnimator
    private lateinit var xMoveAnimator: ValueAnimator

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    private var imageViewPlayerReference: View? = null
    private var playerPosition = MutableLiveData<Rect>()
    private var yPosition = 0F

    // 화면의 크기
    var realX: Int = 0
    var realY: Int = 0

    // 이미지뷰의 크기
    val pWidth = lazy { binding.imageViewPlayer.width }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSensorManager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewPlayerReference = binding.imageViewPlayer

        // 스와이프로 뒤로가기 적용
        binding.layoutSwipe.isSwipeable = true
        swipeCallback = object : SwipeDismissFrameLayout.Callback() {
            override fun onDismissed(layout: SwipeDismissFrameLayout) {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        binding.layoutSwipe.addCallback(swipeCallback)

        val win = mActivity.windowManager.currentWindowMetrics
        realX = win.bounds.width()
        realY = win.bounds.height()

        initListener()

        // 발판 생성
        initPlatform()

        // 점프 애니메이션
        initAnimation()
//        jumpUpAnimation()

    }

    private fun updatePlayerPosition() {
        binding.imageViewPlayer.let {
            val playerRect = Rect(
                binding.imageViewPlayer.x.toInt(),
                binding.imageViewPlayer.y.toInt(),
                (binding.imageViewPlayer.x + binding.imageViewPlayer.width).toInt(),
                (binding.imageViewPlayer.y + binding.imageViewPlayer.height).toInt()
            )
            playerPosition.value = playerRect
        }
    }

    private fun initListener() {
        binding.imageViewPlayer.setOnClickListener {
            Log.d(TAG, "initListener: click")
        }

        playerPosition.observe(viewLifecycleOwner) { playerRect ->

            // 애니메이션 중이 아니라면
            if (!isGoingUp) {
                for (i in 0 until binding.root.childCount) {
                    val platform = binding.root.getChildAt(i)

                    if (platform.tag == "platform" && platform.visibility == View.VISIBLE) {
                        val platformRect = Rect(
                            platform.x.toInt(),
                            platform.y.toInt(),
                            (platform.x + platform.width).toInt(),
                            (platform.y + platform.height).toInt()
                        )

                        if (playerRect.left > platformRect.left && playerRect.right < platformRect.right && playerRect.bottom == platformRect.bottom) {


//                        if (playerRect.intersect(platformRect)) {
                            Log.d(TAG, "checkCollisions:  ${binding.imageViewPlayer.y} ${platform.y}")
                            yPosition = platform.y
                            initAnimation()
                        } else if (!playerRect.intersect(platformRect) && yPosition != 0.0f){
                            fallAnimation()
                        }
                    }
                }
            }
        }
    }


    private fun initPlatform() {
        for (i: Int in 0..2) {
            val platform = createPlatform()
            positionPlatformRandomly(platform)
        }
    }

    private var isGoingUp = true
    private fun initAnimation() {
        valueAnimator = ValueAnimator.ofFloat(yPosition, yPosition - 250f).apply {
            duration = 540
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = DecelerateInterpolator(1.2f)


            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                imageViewPlayerReference?.translationY = animatedValue

                if (isGoingUp) {
                    // 점프해서 위로 올라가는 로직
                    if (animatedValue <= -250f) {
                        isGoingUp = false
                    }
                } else {
                    // 점프한 후 내려오는 로직
                    if (animatedValue >= yPosition) {
                        isGoingUp = true
                    }
                }
            }

//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationStart(animation: Animator) {
//                    super.onAnimationStart(animation)
//                    isAnimating = true
//                }
//
//                override fun onAnimationEnd(animation: Animator) {
//                    super.onAnimationEnd(animation)
//                    isAnimating = false
////                    updatePlayerPosition()
//                }
//            })

            start()
        }
    }

    private var isAnimating = false

    private fun updateAnimation() {
        if (!isAnimating) {
            isAnimating = true
            jumpUpAnimation() // updateAnimation 호출 시 상승하는 애니메이션부터 시작
        }
    }

    private fun jumpUpAnimation() {
        valueAnimator = ValueAnimator.ofFloat(yPosition, yPosition - 200f).apply {
//            duration = 270
            interpolator = DecelerateInterpolator(1.2f)

            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                imageViewPlayerReference?.y = animatedValue
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    jumpDownAnimation() // 점프 상승이 끝나면 내려오는 애니메이션 시작
                }
            })

            start()
        }
    }

    private fun jumpDownAnimation() {
        valueAnimator = ValueAnimator.ofFloat(imageViewPlayerReference?.y ?: 0f, yPosition).apply {
            duration = 270
            interpolator = DecelerateInterpolator(1.2f)

            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                imageViewPlayerReference?.y = animatedValue
                checkPlatformCollision() // 내려오면서 발판과의 충돌 확인
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    isAnimating = false
                }
            })

            start()
        }
    }

    private fun checkPlatformCollision() {
        val playerRect = Rect(
            binding.imageViewPlayer.x.toInt(),
            (binding.imageViewPlayer.y + binding.imageViewPlayer.height).toInt(),
            (binding.imageViewPlayer.x + binding.imageViewPlayer.width).toInt(),
            (binding.imageViewPlayer.y + binding.imageViewPlayer.height).toInt()
        )

        for (i in 0 until binding.root.childCount) {
            val platform = binding.root.getChildAt(i)

            if (platform.tag == "platform" && platform.visibility == View.VISIBLE) {
                val platformRect = Rect(
                    platform.x.toInt(),
                    platform.y.toInt(),
                    (platform.x + platform.width).toInt(),
                    platform.y.toInt()
                )

                if (playerRect.intersect(platformRect)) {
//                    valueAnimator.cancel() // 발판에 닿았을 때 애니메이션 취소
                    yPosition = platform.y
                    imageViewPlayerReference?.y = platform.y + binding.imageViewPlayer.height // 캐릭터 위치를 발판 위로 설정
                    break
                }
            }
        }
    }


    private fun fallAnimation() {
        Log.d(TAG, "fallAnimation: $yPosition")

        valueAnimator = ValueAnimator.ofFloat(yPosition, 0f).apply {
            duration = 270
            interpolator = DecelerateInterpolator(1.2f)

            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
//                imageViewPlayerReference?.translationY = animatedValue
                imageViewPlayerReference?.y = animatedValue
            }

            start()
        }
        yPosition = 0F
    }

    private fun initSensorManager() {
        sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }


    // 메인 스레드에서 돌아감
    private val listener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {
            updatePlayerPosition()
//            Log.d(TAG, "onSensorChanged: ${binding.imageViewPlayer.x}, ${binding.imageViewPlayer.y}")
//            Log.d(TAG, "onSensorChanged: ${binding.imageViewPlayer.left}, ${binding.imageViewPlayer.top}")
//            Log.d(TAG, "onSensorChanged: x: ${event.values[0]}")

            // 캐릭터 방향
            binding.imageViewPlayer.scaleX = if (event.values[0] > 0) -1f else 1f

            // 센서의 움직임에 따라 이동할 위치
            val movement = event.values[0] * 26
            var targetX = binding.imageViewPlayer.x - movement

            if (targetX < -pWidth.value.toFloat()) {
                binding.imageViewPlayer.x = realX.toFloat()
                targetX = (realX - binding.imageViewPlayer.width).toFloat() - movement
            } else if (targetX > realX) {
                binding.imageViewPlayer.x = -pWidth.value.toFloat()
                targetX = movement
            }

            xMoveAnimator =
                ValueAnimator.ofFloat(imageViewPlayerReference?.x ?: 0F, targetX).apply {
                    duration = 150L
                    interpolator = DecelerateInterpolator()
                    addUpdateListener { animation ->
                        val animatedValue = animation.animatedValue as Float
                        imageViewPlayerReference?.x = animatedValue
                    }
                    start()
                }

        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

    }

    private fun positionPlatformRandomly(platform: ImageView) {
        val maxX = realX - platform.width
        val randomX = (0..maxX).random()
        val minY = 0f
        val maxY = (realY - platform.height).toFloat()
        val randomY = (minY.toInt()..maxY.toInt()).random()

        platform.x = randomX.toFloat()
        platform.y = randomY.toFloat()
        platform.visibility = View.VISIBLE
        Log.d(TAG, "positionPlatformRandomly: ${platform.x} ${platform.y}")
    }


    private fun createPlatform(): ImageView {
        val platform = ImageView(context)
        platform.setImageResource(R.drawable.icon_foothold)
        platform.layoutParams = RelativeLayout.LayoutParams(
            dpToPx(40f).toInt(), dpToPx(40f).toInt()
        )
        platform.visibility = View.INVISIBLE

        // 발판에 태그 설정
        platform.tag = "platform"

        // 발판을 레이아웃에 추가
        binding.root.addView(platform)

        return platform
    }

    private fun dpToPx(dp: Float): Float {
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(listener)
        valueAnimator.cancel()
        xMoveAnimator.cancel()
    }

    override fun onDestroyView() {
        imageViewPlayerReference = null
        binding.layoutSwipe.removeCallback(swipeCallback)
        super.onDestroyView()
    }

}