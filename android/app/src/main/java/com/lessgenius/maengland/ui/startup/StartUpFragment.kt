package com.lessgenius.maengland.ui.startup

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.wear.widget.SwipeDismissFrameLayout
import com.lessgenius.maengland.MainViewModel
import com.lessgenius.maengland.R
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.databinding.FragmentStartupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class StartUpFragment : BaseFragment<FragmentStartupBinding>(
    FragmentStartupBinding::bind, R.layout.fragment_startup
) {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var navController: NavController
    private lateinit var swipeCallback: SwipeDismissFrameLayout.Callback

    private lateinit var valueAnimator: ValueAnimator

    private var screenHeight: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutSwipe.isSwipeable = true
        swipeCallback = object : SwipeDismissFrameLayout.Callback() {
            override fun onDismissed(layout: SwipeDismissFrameLayout) {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        binding.layoutSwipe.addCallback(swipeCallback)

        val win = mActivity.windowManager.currentWindowMetrics
        screenHeight = win.bounds.height()

        mainViewModel.getLoginStatus()
        initAnimation()
        initListener()
        navController = Navigation.findNavController(binding.root)

//        initListener()
    }

    private fun initAnimation() {
        binding.imageViewPlayer.post {
            valueAnimator = ValueAnimator().apply {
                setFloatValues(
                    binding.imageViewPlayer.y + binding.imageViewPlayer.y / 2,
                    binding.imageViewPlayer.y - binding.imageViewPlayer.y / 2,
                )
                duration = 460
                interpolator = DecelerateInterpolator(1.2f)
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = DecelerateInterpolator()
                addUpdateListener { animator ->
                    val animatedValue = animator.animatedValue as Float
                    binding.imageViewPlayer.y = animatedValue
                }
                start()
            }
        }

    }

    private fun initListener() {

        binding.buttonSetting.setOnClickListener {
            if (mainViewModel.loginStatusResponse.value) {
                navController.navigate(R.id.action_startUpFragment_to_mypageFragment)
            } else {
                navController.navigate(R.id.action_startUpFragment_to_loginFragment)
            }
        }

        binding.buttonGame.setOnClickListener {
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
        super.onDestroyView()
        valueAnimator.cancel()
//        binding.layoutSwipe.removeCallback(swipeCallback)
    }
}