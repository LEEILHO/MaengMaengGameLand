package com.lessgenius.maengland.ui.game

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
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.wear.widget.SwipeDismissFrameLayout
import com.lessgenius.maengland.R
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "GameFragment_김진영"

@AndroidEntryPoint
class GameFragment :
    BaseFragment<FragmentGameBinding>(FragmentGameBinding::bind, R.layout.fragment_game) {

    private val gameViewModel: GameViewModel by viewModels()

    companion object {
        const val JUMP_HEIGHT = 220F
    }

    private lateinit var swipeCallback: SwipeDismissFrameLayout.Callback

    // 센서
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    // 애니메이션
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
    private var score = MutableLiveData(0)
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
        binding.gameScrollView.setOnTouchListener { _, _ -> true }

        binding.imageViewPlayer.bringToFront()
        binding.textviewScore.bringToFront()

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

        // 스크롤 뷰
        binding.gameLayout.layoutParams.height = screenHeight * 120
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

        score.observe(viewLifecycleOwner) { newScore ->
            // 현재 표시된 점수를 가져오기 (초기값은 '0'으로 가정)
            val currentScore = binding.textviewScore.text.toString().toIntOrNull() ?: 0

            // ValueAnimator를 사용하여 현재 점수에서 새 점수까지 애니메이션을 적용합니다.
            val scoreAnimator = ValueAnimator.ofInt(currentScore, newScore).apply {
                duration = 500 // 애니메이션 지속 시간을 설정합니다.
                addUpdateListener { animator ->
                    // 현재 애니메이션 값으로 텍스트 뷰를 업데이트합니다.
                    binding.textviewScore.text = animator.animatedValue.toString()
                }
            }

            scoreAnimator.start()
        }

    }

    private var isCollided = false  // 충돌 감지 플래그


    private fun checkVisiblePlatforms(playerRect: Rect) {
        val scrollY = binding.gameScrollView.scrollY  // 현재 스크롤 위치
        val visibleHeight = binding.gameScrollView.height  // 스크롤뷰의 가시 범위 높이

        // 스크롤된 영역 아래의 발판을 찾기 위한 위치
        val thresholdY = scrollY + visibleHeight

        for (i in 0 until binding.gameLayout.childCount) {
            val platform = binding.gameLayout.getChildAt(i) ?: continue

            if (platform.tag == "platform") {
                val platformTop = platform.y

                if (platformTop > thresholdY) {
                    platform.visibility = View.GONE
                } else if (platform.visibility == View.VISIBLE) {
                    // 화면에 보이는 발판 처리 로직
                    val platformRect = Rect(
                        platform.x.toInt(),
                        platformTop.toInt(),
                        (platform.x + platform.width).toInt(),
                        (platformTop + platform.height).toInt()
                    )

                    if (playerRect.right > platformRect.left && playerRect.left < platformRect.right && (playerRect.bottom <= platformRect.top) && (platformRect.top - playerRect.bottom <= 40)) {

//                        // 위로 올라갔을 때
                        if (yPosition > platformRect.top.toFloat()) {
                            Log.d(TAG, "initObserve: scroll")
                            binding.gameScrollView.smoothScrollBy(
                                0,
                                -(yPosition - platformRect.top).toInt()
                            )
                            score.value =
                                score.value?.plus(((yPosition - platformRect.top) / 10).toInt())
                            Log.d(
                                TAG,
                                "checkVisiblePlatforms: ${score.value} ${yPosition - platformRect.top}"
                            )
                        }

                        yPosition = platformRect.top.toFloat()
                        Log.d(TAG, "checkVisiblePlatforms: $playerRect $platformRect")
                        isCollided = true // 충돌 발생 플래그

                        fallDownAnimator.cancel()
                        jumpUpAnimator.setFloatValues(yPosition, yPosition - JUMP_HEIGHT)
                        fallDownAnimator.setFloatValues(
                            yPosition - JUMP_HEIGHT,
                            yPosition + screenHeight
                        )
                        jumpUpAnimator.start()
                        break
                    }
                }
            }
        }

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
            interpolator = DecelerateInterpolator(1.8f)
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                player?.y = animatedValue - player?.height!!
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    updatePlayerPosition()
                    initFallDownAnimation()
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
                    duration = 615
                    interpolator = LinearInterpolator()
                    addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue as Float
                        player?.y = animatedValue - player?.height!!
                        updatePlayerPosition()
                    }
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            if (!isCollided) {
                                // 충돌하지 않았으면 추락 처리
                                Log.d(TAG, "fallDownAnimator: 추락! 플레이어의 높이를 0으로 설정")
                                onPlayerFell() // 추락
                            } else {
                                // 내려와서 충돌했다면 새로운 점프를 준비
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
        sensorManager.unregisterListener(listener)
        jumpUpAnimator.removeAllUpdateListeners()
        jumpUpAnimator.cancel()
        fallDownAnimator.removeAllUpdateListeners()
        fallDownAnimator.cancel()
        xMoveAnimator.removeAllUpdateListeners()
        xMoveAnimator.cancel()

        // 플레이어의 추락 상태
        player?.rotation = 90f
        player?.scaleX = 1f

        // 플레이어가 있는 위치로 ScrollView를 스크롤
        val playerYPosition = (player?.y ?: 0f) - binding.gameScrollView.height + player?.height!!
        binding.gameScrollView.post {
            binding.gameScrollView.smoothScrollTo(0, playerYPosition.toInt())
        }

        gameViewModel.recordScore(score.value ?: 0)
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
            val movement = event.values[0] * 24
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
            layoutParams.width = originalWidth / 4
            layoutParams.height = originalHeight / 4
        }

        platform.visibility = View.GONE

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
            platform.x = (screenWidth / 2 - platform.layoutParams.width / 2).toFloat()
            player?.apply {
                y = platform.y + platform.height
            }
            yPosition = player?.y!!
            Log.d(TAG, "init yPosition: ${player?.x} ${player?.y}")
            Log.d(TAG, "init yPosition: ${platform.x} ${platform.y}")
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(listener)
        xMoveAnimator.cancel()
    }

    override fun onDestroyView() {
        player = null
        binding.layoutSwipe.removeCallback(swipeCallback)
        super.onDestroyView()
    }

}