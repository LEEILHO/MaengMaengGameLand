package com.lessgenius.maengland

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.lessgenius.maengland.base.BaseFragment
import com.lessgenius.maengland.databinding.FragmentStartupBinding

class StartUpFragment : BaseFragment<FragmentStartupBinding>(
    FragmentStartupBinding::bind, R.layout.fragment_startup
) {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)

        initListener()
    }

    private fun initListener() {

        binding.buttonLogin.setOnClickListener {
            navController.navigate(R.id.action_startUpFragment_to_loginFragment)
        }
        binding.buttonGame.setOnClickListener {
            navController.navigate(R.id.action_startUpFragment_to_gameFragment)
        }
    }
}