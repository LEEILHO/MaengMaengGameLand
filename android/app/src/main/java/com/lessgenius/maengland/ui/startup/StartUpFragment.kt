package com.lessgenius.maengland.ui.startup

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.wear.widget.SwipeDismissFrameLayout
import com.lessgenius.maengland.MainViewModel
import com.lessgenius.maengland.R
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.databinding.FragmentStartupBinding
import com.lessgenius.maengland.util.SoundUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartUpFragment : BaseFragment<FragmentStartupBinding>(
    FragmentStartupBinding::bind, R.layout.fragment_startup
) {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var navController: NavController
    private lateinit var swipeCallback: SwipeDismissFrameLayout.Callback

    private lateinit var valueAnimator: ValueAnimator

    private var player: ImageView? = null
    private var screenHeight: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initAnimation()
        initListener()
    }

    val handler = Handler(Looper.getMainLooper())

    private fun initData() {
        binding.layoutSwipe.isSwipeable = true
        swipeCallback = object : SwipeDismissFrameLayout.Callback() {
            override fun onDismissed(layout: SwipeDismissFrameLayout) {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        binding.layoutSwipe.addCallback(swipeCallback)

        navController = Navigation.findNavController(binding.root)

        val win = mActivity.windowManager.currentWindowMetrics
        screenHeight = win.bounds.height()

        mainViewModel.getLoginStatus()

        player = binding.imageViewPlayer
    }
    private fun initAnimation() {
        var beforeAnimateValue = 0F

        player?.post {
            valueAnimator = ValueAnimator().apply {
                setFloatValues(
                    player?.y?.plus(binding.imageViewPlayer.y / 2) ?: 0F,
                    player?.y?.minus(binding.imageViewPlayer.y / 2) ?: 0F
                )
                duration = 460
                interpolator = DecelerateInterpolator(1.2f)
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = DecelerateInterpolator()

                addUpdateListener { animator ->
                    val animatedValue = animator.animatedValue as Float
                    player?.y = animatedValue

                    if (animatedValue >= beforeAnimateValue) { // 내려가는 중
                        handler.postDelayed({
                            player?.setImageResource(R.drawable.icon_bunny)
                        }, 60)
                    } else { // 점프하는 중
                        player?.setImageResource(R.drawable.icon_bunny_jump)
                    }
                    beforeAnimateValue = animatedValue

                }
                start()
            }
        }

    }

    private fun initListener() {

        binding.buttonSetting.setOnClickListener {
            SoundUtil.playClickSound()
            if (mainViewModel.loginStatusResponse.value) {
                navController.navigate(R.id.action_startUpFragment_to_mypageFragment)
            } else {
                navController.navigate(R.id.action_startUpFragment_to_loginFragment)
            }
        }

        binding.buttonGame.setOnClickListener {
            SoundUtil.playClickSound()
            navController.navigate(R.id.action_startUpFragment_to_gameFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::valueAnimator.isInitialized && valueAnimator.isPaused) {
            valueAnimator.start()
        }
    }

    override fun onPause() {
        super.onPause()
        valueAnimator.cancel()
    }

    override fun onDestroyView() {
        binding.layoutSwipe.removeCallback(swipeCallback)
        super.onDestroyView()
        valueAnimator.cancel()
        player = null

    }
}