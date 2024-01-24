package com.example.newconverterapp.ui.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newconverterapp.R
import com.example.newconverterapp.databinding.DialogResultBinding

class ResultDialog (private val title: String, private val description: String): DialogFragment() {

    private var _binding: DialogResultBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogResultBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.tvTitle.text = title
        binding.tvDescription.text = description
        binding.btnDone.setOnClickListener { dismiss() }
    }

}