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
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.lessgenius.maengland.R
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.databinding.FragmentGameBinding
import com.lessgenius.maengland.util.SoundUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


private const val TAG = "GameFragment_김진영"

@AndroidEntryPoint
class GameFragment :
    BaseFragment<FragmentGameBinding>(FragmentGameBinding::bind, R.layout.fragment_game),
    GameDialogInterface {

    private val gameViewModel: GameViewModel by viewModels()

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

    private val JUMP_HEIGHT = 220F

    private var player: ImageView? = null
    val pWidth = lazy { binding.imageViewPlayer.width }

    @Volatile
    private var playerPosition = MutableLiveData<Rect>()
    private var yPosition = 0F
    private var playerMinHeight = 0F // 플레이어의 최고 높이

    private var lastPlatformY = 0
    private var lastStarY = 0
    private var score = MutableLiveData(0)

    val handler = Handler(Looper.getMainLooper())


    // 화면의 크기
    var screenWidth: Int = 0
    var screenHeight: Int = 0

    private var isCollided = false  // 충돌 감지 플래그


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

        val win = mActivity.windowManager.currentWindowMetrics
        screenWidth = win.bounds.width()
        screenHeight = win.bounds.height()

        // 스크롤 뷰
        binding.gameLayout.layoutParams.height = screenHeight * 500
        binding.gameScrollView.post {
            binding.gameScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

    }

    private fun initObserve() {
        playerPosition.observe(viewLifecycleOwner) { playerRect ->
            if (isGoingDown) { // 점프하고 내려오는 중
                checkVisiblePlatforms(playerRect)
            }

            // 별 충돌
            checkColliedStar(playerRect)
        }

        score.observe(viewLifecycleOwner) { newScore ->
            // 현재 표시된 점수를 가져오기
            val currentScore = binding.textviewScore.text.toString().toIntOrNull() ?: 0

            val scoreAnimator = ValueAnimator.ofInt(currentScore, newScore).apply {
                duration = 650
                addUpdateListener { animator ->
                    binding.textviewScore.text = animator.animatedValue.toString()
                }
            }

            scoreAnimator.start()
        }

    }

    private fun checkColliedStar(playerRect: Rect) {
        for (i in 0 until binding.gameLayout.childCount) {
            val child = binding.gameLayout.getChildAt(i) ?: continue
            if (child.tag == "star") { // star
                if (child.visibility == View.VISIBLE) {
                    val starTop = child.y

                    val starRect = Rect(
                        child.x.toInt(),
                        starTop.toInt(),
                        (child.x + child.width).toInt(),
                        (starTop + child.height).toInt()
                    )

                    if (Rect.intersects(starRect, playerRect)) {
                        Log.d(TAG, "star!")
                        score.value = score.value?.plus(50)
                        child.visibility = View.GONE
                        SoundUtil.playStarSound()
                    }
                }
            }
        }
    }


    private fun checkVisiblePlatforms(playerRect: Rect) {
        val scrollY = binding.gameScrollView.scrollY  // 현재 스크롤 위치
        val visibleHeight = binding.gameScrollView.height  // 스크롤뷰의 가시 범위 높이

        // 스크롤된 영역 아래의 발판을 찾기 위한 위치
        val thresholdY = scrollY + visibleHeight

        for (i in 0 until binding.gameLayout.childCount) {
            val child = binding.gameLayout.getChildAt(i) ?: continue

            if (child.tag == "normal" || child.tag == "transparent") {
                val platformTop = child.y

                if (platformTop > thresholdY) {
                    child.visibility = View.GONE
                } else if (child.visibility == View.VISIBLE) { // 화면에 보이는 발판 처리 로직

                    val platformRect = Rect(
                        child.x.toInt(),
                        platformTop.toInt(),
                        (child.x + child.width).toInt(),
                        (platformTop + child.height).toInt()
                    )

                    if (playerRect.right > platformRect.left && playerRect.left < platformRect.right && (playerRect.bottom <= platformRect.top) && (platformRect.top - playerRect.bottom <= 40)) {
                        Log.d(TAG, "checkVisiblePlatforms: $playerRect $platformRect")
                        SoundUtil.playJumpSound()
                        onCollidedPlatform(platformRect.top.toFloat())

                        if (child.tag == "transparent") {
                            handler.postDelayed({
                                child.visibility = View.GONE
                            }, 50)
                        }

                        break
                    }
                }
            }
        }
    }

    private fun onCollidedPlatform(platformTop: Float) {
        if (playerMinHeight > platformTop) { // 최고 높이 갱신

            binding.gameScrollView.smoothScrollBy(
                0, -(yPosition - platformTop).toInt()
            )

            score.value =
                score.value?.plus(((yPosition - platformTop) / 10).toInt())
            playerMinHeight = platformTop

            // 새로운 발판 생성
            while (platformTop - lastPlatformY < screenHeight) {
                createNewPlatform()
            }

            // 별 생성
            createNewStar()

        }

        yPosition = platformTop
        isCollided = true

        fallDownAnimator.cancel()
        jumpUpAnimator.setFloatValues(yPosition, yPosition - JUMP_HEIGHT)
        fallDownAnimator.setFloatValues(
            yPosition - JUMP_HEIGHT,
            yPosition + screenHeight
        )
        jumpUpAnimator.start()
    }


    private fun updatePlayerPosition() {
        player?.let {
            val x = it.x
            val y = it.y
            val width = it.width
            val height = it.height

            playerPosition.value =
                Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
        }
    }


    private var isGoingDown = false
    private lateinit var jumpUpAnimator: ValueAnimator
    private lateinit var fallDownAnimator: ValueAnimator

    private fun initJumpUpAnimation() {
        jumpUpAnimator = ValueAnimator.ofFloat(yPosition, yPosition - JUMP_HEIGHT).apply {
            duration = 800
            interpolator = DecelerateInterpolator(1.8f)
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                player?.y = animatedValue - player?.height!!
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    player?.setImageResource(R.drawable.icon_bunny)

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
                    duration = 700
                    interpolator = LinearInterpolator()
                    addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue as Float
                        player?.y = animatedValue - player?.height!!
                        updatePlayerPosition()
                    }
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)

                            handler.postDelayed({
                                player?.setImageResource(R.drawable.icon_bunny_jump)
                            }, 100)

                            if (!isCollided) {
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
        cancelAnimation()

        // 플레이어의 추락 상태
        player?.rotation = 120f
        player?.scaleX = 1f

        // 플레이어가 있는 위치로 ScrollView를 스크롤
        val playerYPosition = (player?.y ?: 0f) - binding.gameScrollView.height + player?.height!!
        binding.gameScrollView.post {
            binding.gameScrollView.smoothScrollTo(0, playerYPosition.toInt())
        }

        gameViewModel.recordScore(score.value ?: 0)

        val dialog = GameDialog(this, score.value ?: 0)
        handler.postDelayed({
            dialog.isCancelable = false
            dialog.show(this.parentFragmentManager, "GameDialog")
        }, 300)

    }

    private fun initSensorManager() {
        sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }


    private val listener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {

            // 캐릭터 좌우 방향
            player?.scaleX = if (event.values[0] > 0) 1f else -1f

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

    private fun initPlatform() {
        lastPlatformY = binding.gameLayout.layoutParams.height  // 가장 아래 발판의 Y 위치
        lastStarY = binding.gameLayout.layoutParams.height - screenHeight // 별 시작 Y 위치
        val initPlatformY = binding.gameLayout.layoutParams.height - screenHeight * 2

        val MIN_PLATFORM_DISTANCE = JUMP_HEIGHT / 3  // 최소 간격
        val MAX_PLATFORM_DISTANCE = JUMP_HEIGHT      // 최대 간격

        var idx = 0
        while (lastPlatformY > initPlatformY) {
            val distance = if (idx == 0) {
                MIN_PLATFORM_DISTANCE.toInt()
            } else (MIN_PLATFORM_DISTANCE.toInt()..MAX_PLATFORM_DISTANCE.toInt()).random()

            lastPlatformY -= distance

            val platform = createPlatform()
            positionPlatformRandomly(platform, lastPlatformY, idx++)

        }
    }

    private fun createNewPlatform() {

        val MIN_PLATFORM_DISTANCE = JUMP_HEIGHT / 3  // 최소 간격
        val MAX_PLATFORM_DISTANCE = JUMP_HEIGHT      // 최대 간격

        val distance = (MIN_PLATFORM_DISTANCE.toInt()..MAX_PLATFORM_DISTANCE.toInt()).random()

        lastPlatformY -= distance

        val platform = createPlatform()
        positionPlatformRandomly(platform, lastPlatformY)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createPlatform(): ImageView {

        val platform = ImageView(context).apply {

            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            if (lastPlatformY < binding.gameLayout.layoutParams.height - screenHeight * 3) {
                if (Random.nextBoolean()) {
                    setImageResource(R.drawable.icon_foothold)
                    tag = "normal"
                } else {
                    setImageResource(R.drawable.icon_foothold)
                    alpha = 0.4F
                    tag = "transparent"
                }

            } else {
                tag = "normal"
                setImageResource(R.drawable.icon_foothold)
            }

            // 이미지 드로어블의 원본 크기를 가져옴
            val drawable =
                context?.resources?.getDrawable(R.drawable.icon_foothold, null) as BitmapDrawable
            val originalWidth = drawable.intrinsicWidth
            val originalHeight = drawable.intrinsicHeight

            // 이미지의 크기 설정
            layoutParams.width = originalWidth / 4
            layoutParams.height = originalHeight / 4
        }

        platform.visibility = View.VISIBLE

        // 발판을 레이아웃에 추가
        binding.gameLayout.addView(platform)

        player?.bringToFront()

        return platform
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createStar(): ImageView {

        val star = ImageView(context).apply {
            setImageResource(R.drawable.icon_star)

            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            val drawable =
                context?.resources?.getDrawable(R.drawable.icon_star, null) as BitmapDrawable
            val originalWidth = drawable.intrinsicWidth
            val originalHeight = drawable.intrinsicHeight

            layoutParams.width = originalWidth / 24
            layoutParams.height = originalHeight / 24

            tag = "star"
        }

        star.visibility = View.VISIBLE
        binding.gameLayout.addView(star)
        player?.bringToFront()

        return star
    }

    private fun createNewStar() {
        val MIN_STAR_DISTANCE = JUMP_HEIGHT * 2     // 최소 간격
        val MAX_STAR_DISTANCE = JUMP_HEIGHT * 3  // 최대 간격

        val distance = (MIN_STAR_DISTANCE.toInt()..MAX_STAR_DISTANCE.toInt()).random()

        lastStarY -= distance

        val star = createStar()
        positionPlatformRandomly(star, lastStarY)
    }

    private fun positionPlatformRandomly(imageView: ImageView, targetY: Int, index: Int = -1) {
        val maxX = screenWidth - imageView.layoutParams.width
        val randomX = (0..maxX).random()

        imageView.x = randomX.toFloat()
        imageView.y = targetY.toFloat()

        // 플레이어의 위치를 시작 발판의 위치로 지정
        if (index == 0) {
            imageView.x = (screenWidth / 2 - imageView.layoutParams.width / 2).toFloat()
            player?.apply {
                y = imageView.y + imageView.height
            }
            yPosition = player?.y!!
            playerMinHeight = yPosition

            Log.d(TAG, "init yPosition: ${player?.x} ${player?.y}")
            Log.d(TAG, "init yPosition: ${imageView.x} ${imageView.y}")
        }
    }

    private fun cancelAnimation() {
        sensorManager.unregisterListener(listener)
        jumpUpAnimator.removeAllUpdateListeners()
        jumpUpAnimator.cancel()
        fallDownAnimator.removeAllUpdateListeners()
        fallDownAnimator.cancel()
        xMoveAnimator.removeAllUpdateListeners()
        xMoveAnimator.cancel()
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
        super.onDestroyView()
    }

    override fun onHomeButtonClick() {
        SoundUtil.playClickSound()
        findNavController().popBackStack()
    }


}