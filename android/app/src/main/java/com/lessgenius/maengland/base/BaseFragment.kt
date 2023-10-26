package com.lessgenius.maengland.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lessgenius.maengland.MainActivity

// Fragment의 기본을 작성, 뷰 바인딩 활용
abstract class BaseFragment<B : ViewBinding>(
    private val bind: (View) -> B,
    @LayoutRes layoutResId: Int
) : Fragment(layoutResId) {
    private var _binding: B? = null

    protected val binding get() = _binding!!
    private lateinit var _mActivity: MainActivity
    protected val mActivity get() = _mActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bind(super.onCreateView(inflater, container, savedInstanceState)!!)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _mActivity = context as MainActivity
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}