package com.example.newconverterapp.ui.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newconverterapp.R
import com.example.newconverterapp.databinding.DialogCurrencySelectorBinding

class SelectorDialog(private val title: String, private val items: List<String>, private val onGetCurrency: (String) -> Unit) : DialogFragment() {

    private var _binding: DialogCurrencySelectorBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogCurrencySelectorBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() = with(binding){
        isCancelable = false
        val array = items.toTypedArray()
        tvTitle.text = title
        npPicker.minValue = 0
        npPicker.maxValue = items.size - 1
        npPicker.displayedValues = array
        btnSelect.setOnClickListener {
            val selectedItem = items[npPicker.value]
            onGetCurrency(selectedItem)
            dismiss()
        }
    }
}