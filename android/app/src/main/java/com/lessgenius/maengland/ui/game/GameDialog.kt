package com.lessgenius.maengland.ui.game

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lessgenius.maengland.databinding.DialogGameBinding

class GameDialog(gameDialogInterface: GameDialogInterface, score: Int) : DialogFragment() {

    private var _binding: DialogGameBinding? = null
    private val binding get() = _binding!!

    private var gameDialogInterface: GameDialogInterface? = null
    private var score: Int? = null

    init {
        this.gameDialogInterface = gameDialogInterface
        this.score = score
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGameBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initListener()

        return binding.root
    }

    private fun initListener() {

        binding.textviewScore.text = score.toString() + "점 달성!"

        binding.buttonHome.setOnClickListener {
            this.gameDialogInterface?.onHomeButtonClick()
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface GameDialogInterface {
    fun onHomeButtonClick()
}