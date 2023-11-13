package com.lessgenius.maengland.ui.login

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.wear.widget.SwipeDismissFrameLayout
import com.lessgenius.maengland.R
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.data.model.NetworkResult
import com.lessgenius.maengland.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "LoginFragment_김진영"

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModels()

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
        initObserver()
    }


    private fun initListener() {
//        loginViewModel.getLoginStatus()
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
                        loginViewModel.updateToken(result.data)
                    }

                    is NetworkResult.Error -> {
                        Log.d(TAG, "로그인 실패!")
                        Toast.makeText(mActivity, "코드를 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.loginStatusResponse.collect { result ->
                if (result != null && result.watchAccessToken.isNotEmpty()) {
                    if (navController.currentDestination?.id == R.id.loginFragment) {
                        navController.navigate(R.id.action_loginFragment_to_mypageFragment)
                    }
                }

                Log.d(TAG, "initObserver: $result")
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
//        binding.layoutSwipe.removeCallback(swipeCallback)
    }


}