package com.lessgenius.maengland.ui.mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.wear.widget.SwipeDismissFrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lessgenius.maengland.R
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.data.model.User
import com.lessgenius.maengland.databinding.FragmentMypageBinding
import com.lessgenius.maengland.ui.game.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MypageFragment_김진영"

@AndroidEntryPoint
class MypageFragment :
    BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage) {

    private val mypageViewModel: MypageViewModel by viewModels()

    private lateinit var swipeCallback: SwipeDismissFrameLayout.Callback

    private val navController by lazy {
        Navigation.findNavController(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutSwipe.isSwipeable = true
        swipeCallback = object : SwipeDismissFrameLayout.Callback() {
            override fun onDismissed(layout: SwipeDismissFrameLayout) {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        binding.layoutSwipe.addCallback(swipeCallback)
        initListener()
        initObserve()
//        showLoadingDialog(mActivity)
        mypageViewModel.getUserInfo()
        mypageViewModel.getBestScore()
    }

    private fun initListener() {
        binding.buttonLogout.setOnClickListener {
            mypageViewModel.logout()
            viewLifecycleOwner.lifecycleScope.launch {
                mypageViewModel.loginStatusResponse.collect { result ->
                    Log.d(TAG, "initObserver: $result")
                    if (!result) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            mypageViewModel.userInfo.collect { result ->
                Log.d(TAG, "initObserve: $result")
                when (result) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "initObserve: ${result.data}")
                        setUserProfile(result.data)
                    }

                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mypageViewModel.bestScoreInfo.collect { result ->
                Log.d(TAG, "bestScore: $result")
                when (result) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "bestScore: ${result.data}")
                        binding.textviewScore.text = result.data.score.toString()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setUserProfile(user: User) {
        Glide.with(binding.imageviewProfile)
            .load(user.profile)
            .addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    dismissLoadingDialog()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
//                    dismissLoadingDialog()
                    return false
                }

            })
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .into(binding.imageviewProfile)


        binding.textviewNickname.text = user.nickname
    }


}