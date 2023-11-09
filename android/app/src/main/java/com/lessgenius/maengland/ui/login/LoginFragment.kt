package com.lessgenius.maengland.ui.login

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.wear.widget.SwipeDismissFrameLayout
import com.lessgenius.maengland.R
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "LoginFragment_김진영"
@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var swipeCallback: SwipeDismissFrameLayout.Callback

    private lateinit var valueAnimator: ValueAnimator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        initObserver()
    }


    private fun initListener() {
        binding.edittextCode.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d(TAG, "initListener: enter")
                loginViewModel.login(binding.edittextCode.text.toString())
            }
            false
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.loginResponse.collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "로그인 성공!: ${result.data}")
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding.layoutSwipe.removeCallback(swipeCallback)
    }


}