package com.lessgenius.maengland

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
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

    companion object {
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

    private var player: View? = null

    @Volatile
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
        initJumpUpAnimation()


        // 스크롤 금지
//        binding.gameScrollView.setOnTouchListener { _, _ -> true }

//        binding.imageViewPlayer.bringToFront()
//        binding.root.bringChildToFront(binding.imageViewPlayer)
//        binding.imageViewPlayer.elevation = 1f
    }

    private fun initData() {
        player = binding.imageViewPlayer

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
//        Log.d(TAG, "onViewCreated: $screenWidth $screenHeight")

        // 스크롤 뷰
        binding.gameLayout.layoutParams.height = screenHeight * 2
        binding.gameScrollView.post {
            binding.gameScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

        Log.d(
            TAG,
            "onViewCreated: ${binding.gameLayout.layoutParams.height} ${binding.gameLayout.layoutParams.width}"
        )
    }

    private fun initObserve() {
        playerPosition.observe(viewLifecycleOwner) { playerRect ->
            // 점프하고 내려오는 중
            if (isGoingDown) {
                checkVisiblePlatforms(playerRect)
            }
        }
    }

    private var isCollided = false  // 충돌 감지 플래그

    private fun checkVisiblePlatforms(playerRect: Rect) {
        val scrollY = binding.gameScrollView.scrollY  // 현재 스크롤 위치
        val visibleHeight = binding.gameScrollView.height  // 스크롤뷰의 가시 범위 높이

        // 화면에 보이는 범위의 상단과 하단 좌표
        val visibleTop = scrollY
        val visibleBottom = scrollY + visibleHeight

        for (i in 0 until binding.gameLayout.childCount) {
            val platform = binding.gameLayout.getChildAt(i)

            if (platform.tag == "platform" && platform.visibility == View.VISIBLE) {
                // 발판의 위치가 화면에 보이는 범위 안에 있는지 확인합니다.
                if (platform.y + platform.height >= visibleTop && platform.y <= visibleBottom) {
//                    Log.d(TAG,"checkVisiblePlatforms: $i ${platform.y} ${platform.height}, visible $visibleTop $visibleBottom")

                    // 이 범위 내의 발판만 체크하고, 필요한 로직을 수행합니다.
                    val platformRect = Rect(
                        platform.x.toInt(),
                        platform.y.toInt(),
                        (platform.x + platform.width).toInt(),
                        (platform.y + platform.height).toInt()
                    )

                    if (playerRect.right > platformRect.left && playerRect.left < platformRect.right && (playerRect.bottom <= platformRect.top) && (platformRect.top - playerRect.bottom <= 40)) {

                        // 위로 올라갔을 때
                        if (yPosition > platformRect.top.toFloat()) {
                            Log.d(TAG, "initObserve: scroll")
                            binding.gameScrollView.smoothScrollBy(
                                0,
                                -(yPosition - platformRect.top).toInt()
                            )
                        }

                        yPosition = platformRect.top.toFloat()
                        Log.d(TAG, "checkVisiblePlatforms: $playerRect $platformRect")
                        isCollided = true // 충돌 발생 플래그

                        fallDownAnimator.cancel()
                        jumpUpAnimator.setFloatValues(yPosition, yPosition - JUMP_HEIGHT)
//                        fallDownAnimator.setFloatValues(yPosition - JUMP_HEIGHT, yPosition)
                        fallDownAnimator.setFloatValues(
                            yPosition - JUMP_HEIGHT,
                            yPosition + screenHeight
                        )
                        jumpUpAnimator.start()
//                        isGoingDown = false
//                        platform.visibility = GONE
                        break
                    }
                }
            }
        }
//        if (!isCollided && isGoingDown) {
//            isCollided = false // 충돌하지 않음 상태 업데이트
//        }
    }


    private fun updatePlayerPosition() {
        player?.let {
            val x = it.x
            val y = it.y
            val width = it.width
            val height = it.height

            // LiveData를 사용하여 위치 업데이트
            playerPosition.value =
                Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
        }
    }


    private var isGoingDown = false
    private lateinit var jumpUpAnimator: ValueAnimator
    private lateinit var fallDownAnimator: ValueAnimator

    private fun initJumpUpAnimation() {
        jumpUpAnimator = ValueAnimator.ofFloat(yPosition, yPosition - JUMP_HEIGHT).apply {
            duration = 815
            interpolator = DecelerateInterpolator(1.2f)
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
//                isGoingDown = false
//                player?.y = animatedValue - player?.height!!
                player?.y = animatedValue - player?.height!!
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    updatePlayerPosition()
//                    isGoingDown = true
//                    fallDownAnimator.setFloatValues(yPosition - JUMP_HEIGHT, yPosition + 100F)
                    initFallDownAnimation()
//                    fallDownAnimator.start()
                    isGoingDown = true
                }
            })
        }
        jumpUpAnimator.start()
    }

    private fun initFallDownAnimation() {
        if (!::fallDownAnimator.isInitialized) { // 초기화
            fallDownAnimator =
                ValueAnimator.ofFloat(yPosition - JUMP_HEIGHT, yPosition + screenHeight).apply {
                    duration = 815 // 내려오는 데 걸리는 시간을 절반으로 설정
                    interpolator = DecelerateInterpolator()
                    addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue as Float
                        player?.y = animatedValue - player?.height!!
                        updatePlayerPosition()
//                        isGoingDown = true // 내려오는 상태
                    }
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            // 내려온 후 처리 로직을 실행합니다.
//                            isGoingDown = false // 내려온 상태 리셋
                            if (!isCollided) {
                                // 충돌하지 않았으면 추락 처리
                                Log.d(TAG, "fallDownAnimator: 추락! 플레이어의 높이를 0으로 설정")
                                onPlayerFell()
                                // 추락 애니메이션 실행
                            } else {
                                // 내려와서 충돌했다면 새로운 점프를 준비합니다.
                                isCollided = false  // 충돌 상태 리셋

                                jumpUpAnimator.setFloatValues(yPosition, yPosition - JUMP_HEIGHT)
                                jumpUpAnimator.start()
                            }
                            isGoingDown = false
                        }
                    })
                }
        }
        fallDownAnimator.setFloatValues(yPosition - JUMP_HEIGHT, yPosition + screenHeight)
        fallDownAnimator.start()
    }


    private fun onPlayerFell() {
        // 애니메이션 관련 리소스를 정리
        // 리스너 해제 및 애니메이션 종료
        sensorManager.unregisterListener(listener)
        jumpUpAnimator.removeAllUpdateListeners()
        jumpUpAnimator.cancel()
        fallDownAnimator.removeAllUpdateListeners()
        fallDownAnimator.cancel()
        xMoveAnimator.removeAllUpdateListeners()
        xMoveAnimator.cancel()
//        player?.visibility = View.GONE // 플레이어를 화면에서 숨깁니다.
    }

    private fun initSensorManager() {
        sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }


    private val listener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {

            // 캐릭터 좌우 방향
            player?.scaleX = if (event.values[0] > 0) -1f else 1f

            // 센서의 움직임에 따라 이동할 위치
            val movement = event.values[0] * 20
            var targetX = player?.x!! - movement

            if (targetX < -pWidth.value.toFloat()) {
                player?.x = screenWidth.toFloat()
                targetX = (screenWidth - player?.width!!).toFloat() - movement
            } else if (targetX > screenWidth) {
                player?.x = -pWidth.value.toFloat()
                targetX = movement
            }
            // 애니메이터 속성 업데이트 및 시작
            if (xMoveAnimator.isRunning) {
                xMoveAnimator.cancel()
            }

            updatePlayerPosition()

            xMoveAnimator.setFloatValues(player?.x!!, targetX)
            xMoveAnimator.start()

        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

    }

    private val MIN_PLATFORM_DISTANCE = JUMP_HEIGHT / 3  // 최소 간격
    private val MAX_PLATFORM_DISTANCE = JUMP_HEIGHT      // 최대 간격

    private fun initPlatform() {
        var lastY = binding.gameLayout.layoutParams.height  // 가장 아래 발판의 Y 위치

        var idx = 0
        while (lastY > 0) {
            val distance =
                (MIN_PLATFORM_DISTANCE.toInt()..MAX_PLATFORM_DISTANCE.toInt()).random()  // 다음 발판까지의 거리
            lastY -= distance

            val platformCount = (1..3).random()  // 발판의 개수
            for (i in 0 until 1) {
                val platform = createPlatform()
                positionPlatformRandomly(platform, lastY, idx++)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createPlatform(): ImageView {

        val platform = ImageView(context).apply {

            setImageResource(R.drawable.icon_foothold)

            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            // 이미지 드로어블의 원본 크기를 가져옴
            val drawable =
                context?.resources?.getDrawable(R.drawable.icon_foothold, null) as BitmapDrawable
            val originalWidth = drawable.intrinsicWidth
            val originalHeight = drawable.intrinsicHeight

            // 이미지의 크기 설정
            layoutParams.width = originalWidth / 3
            layoutParams.height = originalHeight / 3
        }

        platform.visibility = View.INVISIBLE

        // 발판에 태그 설정
        platform.tag = "platform"

        // 발판을 레이아웃에 추가
        binding.gameLayout.addView(platform)

        return platform
    }

    private fun positionPlatformRandomly(platform: ImageView, targetY: Int, index: Int) {
        val maxX = screenWidth - platform.layoutParams.width
        val randomX = (0..maxX).random()

        platform.x = randomX.toFloat()
        platform.y = targetY.toFloat()
        platform.visibility = View.VISIBLE
        Log.d(
            TAG,
            "positionPlatformRandomly: ${platform.x} ${platform.y} ${platform.layoutParams.height} ${platform.layoutParams.width}"
        )

        // 플레이어의 위치를 시작 발판의 위치로 지정
        if (index == 0) {
            player?.apply {
                x = (platform.x * 2 + platform.width) / 2
                y = platform.y + platform.height
            }
            yPosition = player?.y!!
            Log.d(TAG, "init yPosition: ${player?.x} ${player?.y}")
        }
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
//        valueAnimator.cancel()
        xMoveAnimator.cancel()
    }

    override fun onDestroyView() {
        player = null
        binding.layoutSwipe.removeCallback(swipeCallback)
        super.onDestroyView()
    }

}