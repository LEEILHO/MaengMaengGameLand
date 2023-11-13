package com.lessgenius.maengland.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.wear.widget.SwipeDismissFrameLayout
import com.lessgenius.maengland.R
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.databinding.FragmentMypageBinding
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

    }


}