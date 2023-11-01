package com.lessgenius.maengland

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
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
import android.widget.ScrollView
import androidx.lifecycle.MutableLiveData
import androidx.wear.widget.SwipeDismissFrameLayout
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.databinding.FragmentGameBinding


private const val TAG = "GameFragment_김진영"

class GameFragment :
    BaseFragment<FragmentGameBinding>(FragmentGameBinding::bind, R.layout.fragment_game) {

    companion object{
        const val JUMP_HEIGHT = 250F
    }

    private lateinit var swipeCallback: SwipeDismissFrameLayout.Callback

    // 센서
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    // 애니메이션
    private lateinit var valueAnimator: ValueAnimator
    private val xMoveAnimator: ValueAnimator by lazy {
        ValueAnimator().apply {
            duration = 100
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                binding.imageViewPlayer.x = animatedValue
            }
        }
    }

    private var imageViewPlayerReference: View? = null
    private var playerPosition = MutableLiveData<Rect>()
    private var yPosition = 0F

    // 화면의 크기
    var screenWidth: Int = 0
    var screenHeight: Int = 0

    // 이미지뷰의 크기
    val pWidth = lazy { binding.imageViewPlayer.width }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSensorManager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initObserve()
        initPlatform() // 발판 생성
        initAnimation()


        // 스크롤 금지
//        binding.gameScrollView.setOnTouchListener { _, _ -> true }

//        binding.imageViewPlayer.bringToFront()
//        binding.root.bringChildToFront(binding.imageViewPlayer)
//        binding.imageViewPlayer.elevation = 1f
    }

    private fun initData() {
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
        screenWidth = win.bounds.width()
        screenHeight = win.bounds.height()
        Log.d(TAG, "onViewCreated: $screenWidth $screenHeight")

        // 스크롤 뷰
        binding.gameLayout.layoutParams.height = screenHeight * 2
        binding.gameScrollView.post {
            binding.gameScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

        Log.d(TAG, "onViewCreated: ${binding.gameLayout.layoutParams.height} ${binding.gameLayout.layoutParams.width}")
    }

    private var isCollided = false  // 충돌 감지 플래그
    private fun initObserve() {
        playerPosition.observe(viewLifecycleOwner) { playerRect ->
            // 점프하고 내려오는 중
            if (isGoingDown) {
                for (i in 0 until binding.root.childCount) {
                    val platform = binding.root.getChildAt(i)

                    if (platform.tag == "platform" && platform.visibility == View.VISIBLE) {
                        val platformRect = Rect(
                            platform.x.toInt(),
                            platform.y.toInt(),
                            (platform.x + platform.width).toInt(),
                            (platform.y + platform.height).toInt()
                        )

//                        Log.d(TAG, "initListener: $platformRect")
                        if (playerRect.right > platformRect.left && playerRect.left < platformRect.right && (playerRect.bottom - platformRect.top < 10) && (playerRect.bottom - platformRect.top >= 0)) {
                            Log.d(
                                TAG,
                                "checkCollisions:  ${playerRect.bottom} ${platformRect.top}"
                            )
                            yPosition = platformRect.top.toFloat() - screenHeight
//                            Log.d(TAG, "yPosition: $yPosition")
//                            binding.imageViewPlayer.y = platform.y - binding.imageViewPlayer.height // 플레이어 위치 재조정

                            isCollided = true // 충돌 발생 플래그
                            return@observe
                        }
                    }
                }
            }
        }
    }

    private fun updatePlayerPosition() {
        binding.imageViewPlayer.let {
            val playerRect = Rect(
                it.x.toInt(),
                it.y.toInt(),
                (it.x + it.width).toInt(),
                (it.y + it.height).toInt()
            )
            playerPosition.value = playerRect
        }
    }


    private var isGoingDown = false
    private var beforeAnimateValue = 0F
    private fun initAnimation() {
        if (!::valueAnimator.isInitialized) {
            valueAnimator = ValueAnimator().apply {
                duration = 540
                repeatCount = 1
                repeatMode = ValueAnimator.REVERSE
                interpolator = DecelerateInterpolator(1.2f)

                addUpdateListener { animator ->
                    val animatedValue = animator.animatedValue as Float
                    binding.imageViewPlayer.y = animatedValue + binding.imageViewPlayer.top

                    if (animatedValue >= beforeAnimateValue) {
                        updatePlayerPosition()
                        isGoingDown = true
                    } else {
                        isGoingDown = false
                    }
                    beforeAnimateValue = animatedValue
                }

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        if (isCollided) {
                            // 스크롤뷰의 포지션을 조절
                            // 플레이어의 y 위치 조절
                            binding.imageViewPlayer.y = yPosition - binding.imageViewPlayer.height
                            binding.gameScrollView.smoothScrollBy(0, (yPosition).toInt())
                            Log.d(TAG, "onAnimationEnd: 충돌! $yPosition")
                            isCollided = false
                            initAnimation()  // 충돌 시 애니메이션 다시 시작
                        } else {
                            valueAnimator.setFloatValues(yPosition, yPosition - JUMP_HEIGHT)
                            animation.start() // 애니메이션 다시 시작
                        }
                    }
                })
            }
        }

        valueAnimator.setFloatValues(yPosition, yPosition - JUMP_HEIGHT)
        valueAnimator.start()
    }

    private fun initSensorManager() {
        sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }


    // 메인 스레드에서 돌아감
    private val listener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {
//            updatePlayerPosition()
//            Log.d(TAG, "onSensorChanged: ${binding.imageViewPlayer.x}, ${binding.imageViewPlayer.y}")
//            Log.d(TAG, "onSensorChanged: ${binding.imageViewPlayer.left}, ${binding.imageViewPlayer.top}")
//            Log.d(TAG, "onSensorChanged: x: ${event.values[0]}")

            // 캐릭터 좌우 방향
            binding.imageViewPlayer.scaleX = if (event.values[0] > 0) -1f else 1f

            // 센서의 움직임에 따라 이동할 위치
            val movement = event.values[0] * 26
            var targetX = binding.imageViewPlayer.x - movement

            if (targetX < -pWidth.value.toFloat()) {
                binding.imageViewPlayer.x = screenWidth.toFloat()
                targetX = (screenWidth - binding.imageViewPlayer.width).toFloat() - movement
            } else if (targetX > screenWidth) {
                binding.imageViewPlayer.x = -pWidth.value.toFloat()
                targetX = movement
            }

            // 애니메이터 속성 업데이트 및 시작
            if (xMoveAnimator.isRunning) {
                xMoveAnimator.cancel()
            }

            xMoveAnimator.setFloatValues(binding.imageViewPlayer.x, targetX)
            xMoveAnimator.start()

        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

    }

    private val MIN_PLATFORM_DISTANCE = JUMP_HEIGHT / 3  // 최소 간격
    private val MAX_PLATFORM_DISTANCE = JUMP_HEIGHT      // 최대 간격

    private fun initPlatform() {
        for (i: Int in 0..2) {
            val platform = createPlatform()
            positionPlatformRandomly(platform)
        }
    }

    private fun positionPlatformRandomly(platform: ImageView) {
        val maxX = screenWidth - platform.layoutParams.width
        val randomX = (0..maxX).random()
        val maxY = screenHeight - platform.layoutParams.height
        val randomY = (0..maxY).random()

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
        binding.gameLayout.addView(platform)

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